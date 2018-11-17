package kpi.donate.rest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import kpi.donate.model.dto.AddImageRequestBody;
import kpi.donate.model.dto.Currency;
import kpi.donate.model.entity.Donate;
import kpi.donate.model.entity.Project;
import kpi.donate.repository.DonateRepo;
import kpi.donate.repository.ProjectRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/donate")
public class DonateController {

    private static final String frontUrl = "https://some-front-url";

    private static final String statusProjectClosed = "projectClosed";

    private static final String statusProjectNotFound = "projectNotFound";

    private static final String statusSucceeded = "succeeded";

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    private final ProjectRepo projectRepo;

    private final DonateRepo donateRepo;

    public DonateController(ProjectRepo projectRepo, DonateRepo donateRepo) {
        this.projectRepo = projectRepo;
        this.donateRepo = donateRepo;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @PostMapping(
            value = "/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    private void confirm(
            HttpServletResponse httpServletResponse,
            @RequestParam Map<String, String> chargeRequest) throws StripeException, IOException {
        log.info("confirmDonate '{}'", chargeRequest);

        Long projectId = Long.valueOf(chargeRequest.get("projectId"));
        Long amount = Long.valueOf(chargeRequest.get("amount"));

        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if (!optionalProject.isPresent()) {
            httpServletResponse.sendRedirect(frontUrl + "?status=" + statusProjectNotFound);
            return;
        }
        Project project = optionalProject.get();

        if (!validateProject(httpServletResponse, project)) {
            return;
        }

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("currency", Currency.UAH);
        chargeParams.put("description", "Donate donate for KPI project");
        chargeParams.put("amount", amount);
        chargeParams.put("source", chargeRequest.get("stripeToken"));
        Charge chargeRes = Charge.create(chargeParams);
        log.info("chargeRes '{}'", chargeRes);

        String status = chargeRes.getStatus();
        StringBuilder redirect = new StringBuilder(frontUrl).append("?status=").append(status);

        if (status.equals(statusSucceeded)) {
            String tokenForImageUpload = processPayment(project, amount);
            log.info("'tokenForImageUpload={}'", tokenForImageUpload);
            redirect.append("&token=").append(tokenForImageUpload);
        }

        String redirectUrl = redirect.toString();
        log.info("'redirectUrl={}'", redirect);

        httpServletResponse.sendRedirect(redirectUrl);
    }

    private boolean validateProject(HttpServletResponse httpServletResponse, Project project) {
        try {

            if (!project.isOpen()) {
                httpServletResponse.sendRedirect(frontUrl + "?status=" + statusProjectClosed);
                return false;
            }
            return true;

        } catch (IOException e) {
            throw new RuntimeException("Unable to send a redirect");
        }
    }

    private String processPayment(Project project, Long amount) {

        String tokenForImageUpload = UUID.randomUUID().toString();

        double amountInEntireUnit = amount / 100.;
        double needToCollect = project.getNeededAmount() - project.getCurrentAmount();

        if (needToCollect <= amountInEntireUnit) {
            project.setCurrentAmount(project.getNeededAmount());
            project.setOpen(false);
        } else {
            project.setCurrentAmount(project.getCurrentAmount() + amountInEntireUnit);
        }
        projectRepo.save(project);

        Donate donate = new Donate(tokenForImageUpload, amountInEntireUnit, false, project.getId());
        donateRepo.save(donate);

        return tokenForImageUpload;
    }

    @PostMapping(value = "/image")
    private ResponseEntity<Donate> addImage(@Valid @RequestBody AddImageRequestBody body) {

        Donate donate = donateRepo.findByTokenAndUsed(body.getToken(), false)
                .orElseThrow(() -> new RuntimeException("No donate found for this token"));

        donate.setImage(body.getImage());
        donate.setUsed(true);
        return ResponseEntity.ok(donateRepo.save(donate));
    }
}
