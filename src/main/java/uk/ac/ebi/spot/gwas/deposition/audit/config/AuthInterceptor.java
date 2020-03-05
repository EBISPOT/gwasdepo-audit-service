package uk.ac.ebi.spot.gwas.deposition.audit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.AuthToken;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.AuthTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private AuditServiceConfig auditServiceConfig;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) {
        if (!"/error".equals(httpServletRequest.getRequestURI())) {
            if (!auditServiceConfig.isAuthEnabled()) {
                return true;
            }

            String xAuthHeader = httpServletRequest.getHeader(GeneralCommon.HEADER_X_AUTH);
            if (xAuthHeader == null) {
                xAuthHeader = httpServletRequest.getHeader(GeneralCommon.HEADER_JWT);
            }
            log.info("Received X-Auth header: {}", xAuthHeader);
            if (xAuthHeader != null) {
                Optional<AuthToken> authToken = authTokenRepository.findByToken(xAuthHeader);
                return authToken.isPresent();
            }

        }

        return false;
    }

}
