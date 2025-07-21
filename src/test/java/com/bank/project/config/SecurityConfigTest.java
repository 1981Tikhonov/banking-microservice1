package com.bank.project.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Test
    @com.bank.project.config.WithMockUser(roles = "USER")
    void testAuthenticatedUserCanAccessPublicEndpoints() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mvc.perform(get("/api/public/health"))
                .andExpect(status().isOk());
    }

    private MockMvcConfigurer springSecurity() {
            return null;
    }

    @Test
    @com.bank.project.config.WithMockUser(roles = "USER")
    void testUnauthenticatedUserCannotAccessSecuredEndpoints() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mvc.perform(get("/api/secure/data"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminCanAccessAdminEndpoints() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCsrfProtection() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Without CSRF token should be forbidden
        mvc.perform(post("/api/secure/action"))
                .andExpect(status().isForbidden());

        // With CSRF token should be ok
        mvc.perform(post("/api/secure/action")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    private RequestPostProcessor csrf() {
                return null;
    }
}
