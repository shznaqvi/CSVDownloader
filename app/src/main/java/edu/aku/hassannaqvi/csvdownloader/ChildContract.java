package edu.aku.hassannaqvi.csvdownloader;

import android.provider.BaseColumns;

/**
 * Created by hassan.naqvi on 11/30/2016.
 */

public class ChildContract {

    public static abstract class ChildTable implements BaseColumns {
        public static final String COLUMN_DATE_FORM = "date_form";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_PARENT_STUDY_ID_KEY = "parent_study_id_key";
        public static final String COLUMN_GSED_ID = "gsed_id";
        public static final String COLUMN_CHILD_NAME = "child_name";
        public static final String COLUMN_MOTHER_NAME = "mother_name";
        public static final String COLUMN_FATHER_NAME = "father_name";
        public static final String COLUMN_CHILD_SEX = "child_sex";
        public static final String COLUMN_DOB = "dob";
        public static final String COLUMN_CARE_GIVER = "care_giver";
        public static final String COLUMN_CAREGIVER_NAME = "caregiver_name";
        public static final String COLUMN_STUDY_TYPE = "study_type";
        public static final String COLUMN_CONC_VALIDITY_YESNO = "conc_validity_yesno";
        public static final String COLUMN_RELIABILITY_YESNO = "reliability_yesno";
        public static final String COLUMN_INTERRATE_YESNO = "interrate_yesno";
        public static final String COLUMN_INTRARATER_YESNO = "intrarater_yesno";
        public static final String COLUMN_PRED_VALIDITY_YESNO = "pred_validity_yesno";
        public static final String COLUMN_COVID_MAIN = "COVID_MAIN";
        public static final String COLUMN_DATE_COVID = "date_covid";
        public static final String COLUMN_PSY_MAIN = "PSY_MAIN";
        public static final String COLUMN_DATE_PSY = "date_psy";
        public static final String COLUMN_PSY_IRATER = "PSY_IRATER";
        public static final String COLUMN_PSY_TEST_RETEST = "PSY_TEST_RETEST";
        public static final String COLUMN_PSY_CONCURRENT = "PSY_CONCURRENT";
        public static final String COLUMN_PSY_PREDICTIVE = "PSY_PREDICTIVE";
        public static final String COLUMN_SF_MAIN = "SF_MAIN";
        public static final String COLUMN_DATE_SF = "date_sf";
        public static final String COLUMN_SF_IRATER = "SF_IRATER";
        public static final String COLUMN_SF_TEST_RETEST = "SF_TEST_RETEST";
        public static final String COLUMN_SF_CONCURRENT = "SF_CONCURRENT";
        public static final String COLUMN_SF_PREDICTIVE = "SF_PREDICTIVE";
        public static final String COLUMN_PHQ9_MAIN = "PHQ9_MAIN";
        public static final String COLUMN_DATE_PHQ9 = "date_phq9";
        public static final String COLUMN_CPAS_MAIN = "CPAS_MAIN";
        public static final String COLUMN_DATE_CPAS = "date_cpas";
        public static final String COLUMN_HOME_MAIN = "HOME_MAIN";
        public static final String COLUMN_DATE_HOME = "date_home";
        public static final String COLUMN_ANTHRO_MAIN = "ANTHRO_MAIN";
        public static final String COLUMN_DATE_ANTHRO = "date_anthro";
        public static final String COLUMN_ANTHRO_PREDICTIVE = "ANTHRO_PREDICTIVE";
        public static final String COLUMN_LF_MAIN = "LF_MAIN";
        public static final String COLUMN_DATE_LF = "date_lf";
        public static final String COLUMN_LF_IRATER = "LF_IRATER";
        public static final String COLUMN_LF_TEST_RETEST = "LF_TEST_RETEST";
        public static final String COLUMN_LF_CONCURRENT = "LF_CONCURRENT";
        public static final String COLUMN_LF_PREDICTIVE = "LF_PREDICTIVE";

    }
}
