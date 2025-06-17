package com.example;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@SpringBootApplication
@ImportRuntimeHints(Application.ExamplesRuntimeHints.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * For native build below config is required for serializing {@link org.springframework.security.core.Authentication}.
     * Test api/authentication. It will break without this config.
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}
     * which use the {@link WebAuthenticationDetails}
     */
    static class ExamplesRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection()
                    .registerType(WebAuthenticationDetails.class, MemberCategory.values());
        }

    }

}
