package com.suraev.TaskManagementSystem.util;


import org.springframework.http.HttpHeaders;

    public final class HeaderUtil {

        private HeaderUtil() {
        }

        public static HttpHeaders createAlert(String applicationName, String message, String param) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-" + applicationName + "-alert", message);
            headers.add("X-" + applicationName + "-params", param);
            return headers;
        }

        public static HttpHeaders createFailureAlert(boolean enableTranslation, String defaultMessage) {

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-" + "-error", defaultMessage);

            return headers;
        }
    }
