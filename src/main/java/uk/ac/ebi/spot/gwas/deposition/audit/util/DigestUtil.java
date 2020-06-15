package uk.ac.ebi.spot.gwas.deposition.audit.util;

import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;

public class DigestUtil {

    public static String labelForProvenanceType(String submissionProvenanceType) {
        if (submissionProvenanceType.equalsIgnoreCase(SubmissionProvenanceType.BODY_OF_WORK.name())) {
            return "Body of Work";
        }

        return "Publication";
    }

}
