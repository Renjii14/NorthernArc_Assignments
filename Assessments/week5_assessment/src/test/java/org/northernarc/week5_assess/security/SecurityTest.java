package org.northernarc.week5_assess.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Security layer behaviour")
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    private Map<String, Object> baseRegisterRequest;
    private Map<String, Object> baseLoginRequest;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseRegisterRequest = new HashMap<>();
        baseRegisterRequest.put("name", "John Doe");
        baseRegisterRequest.put("email", "john@example.com");
        baseRegisterRequest.put("password", "pass@123");
        baseRegisterRequest.put("phone", "9876543210");

        baseLoginRequest = new HashMap<>();
        baseLoginRequest.put("email", "john@example.com");
        baseLoginRequest.put("password", "pass@123");

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Nested
    @DisplayName("Public endpoints")
    class PublicEndpoints {

    /*
     * Test Case: shouldAllowRegisterWithoutJwt
     * Description: Validates shouldAllowRegisterWithoutJwt behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldAllowRegisterWithoutJwt() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerRequest())))
                    .andExpect(status().isCreated());

            verify(authenticationProvider, never()).authenticate(any(org.springframework.security.core.Authentication.class));
        }

    /*
     * Test Case: shouldAllowLoginWithoutJwt
     * Description: Validates shouldAllowLoginWithoutJwt behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldAllowLoginWithoutJwt() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest())))
                    .andExpect(status().isOk());

            verify(authenticationProvider, never()).authenticate(any(org.springframework.security.core.Authentication.class));
        }
    }

    @Nested
    @DisplayName("Protected endpoints without valid JWT")
    class InvalidOrMissingToken {

    /*
     * Test Case: shouldReturnUnauthorizedWhenJwtMissing
     * Description: Validates shouldReturnUnauthorizedWhenJwtMissing behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnauthorizedWhenJwtMissing() throws Exception {
            mockMvc.perform(get("/api/customers"))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(authenticationProvider);
        }

    /*
     * Test Case: shouldReturnUnauthorizedWhenAuthorizationHeaderIsNull
     * Description: Validates shouldReturnUnauthorizedWhenAuthorizationHeaderIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnauthorizedWhenAuthorizationHeaderIsNull() throws Exception {
            mockMvc.perform(get("/api/accounts")
                            .header("Authorization", ""))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(authenticationProvider);
        }

    /*
     * Test Case: shouldReturnUnauthorizedWhenAuthorizationHeaderIsBlank
     * Description: Validates shouldReturnUnauthorizedWhenAuthorizationHeaderIsBlank behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnauthorizedWhenAuthorizationHeaderIsBlank() throws Exception {
            mockMvc.perform(get("/api/transactions")
                            .header("Authorization", " "))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(authenticationProvider);
        }

    /*
     * Test Case: shouldReturnUnauthorizedForBearerWithEmptyToken
     * Description: Validates shouldReturnUnauthorizedForBearerWithEmptyToken behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnauthorizedForBearerWithEmptyToken() throws Exception {
            mockMvc.perform(get("/api/customers")
                            .header("Authorization", "Bearer"))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(authenticationProvider);
        }

    /*
     * Test Case: shouldReturnUnauthorizedForBearerWithSpacesOnlyToken
     * Description: Validates shouldReturnUnauthorizedForBearerWithSpacesOnlyToken behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnauthorizedForBearerWithSpacesOnlyToken() throws Exception {
            mockMvc.perform(get("/api/customers")
                            .header("Authorization", "Bearer   "))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(authenticationProvider);
        }

    /*
     * Test Case: shouldReturnUnauthorizedForMalformedOrInvalidTokens
     * Description: Validates shouldReturnUnauthorizedForMalformedOrInvalidTokens behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @ParameterizedTest
        @ValueSource(strings = {
                "invalid-token",
                "abc.def",
                "abc.def.ghi",
                "###.###.###",
                "BearerTokenWithoutSpace",
                "Token abc.def.ghi",
                "Bearer    abc.def.ghi",
                "Bearer verylongtokenverylongtokenverylongtokenverylongtokenverylongtoken"
        })
        void shouldReturnUnauthorizedForMalformedOrInvalidTokens(String headerValue) throws Exception {
            mockMvc.perform(get("/api/accounts/1")
                            .header("Authorization", headerValue))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(authenticationProvider);
        }

    /*
     * Test Case: shouldReturnUnauthorizedForExpiredOrCorruptedBearerTokens
     * Description: Validates shouldReturnUnauthorizedForExpiredOrCorruptedBearerTokens behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @ParameterizedTest
        @ValueSource(strings = {
                "Bearer expired.jwt.token",
                "Bearer wrong.signature.token",
                "Bearer malformed token value",
                "Bearer token-with-invalid-characters-!@#$%^&*()"
        })
        void shouldReturnUnauthorizedForExpiredOrCorruptedBearerTokens(String headerValue) throws Exception {
            mockMvc.perform(get("/api/accounts/1/transactions")
                            .header("Authorization", headerValue))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(authenticationProvider);
        }
    }

    @Nested
    @DisplayName("Valid authentication and authorization")
    class ValidAuthentication {

    /*
     * Test Case: shouldAllowAccessToProtectedEndpointForAuthenticatedUser
     * Description: Validates shouldAllowAccessToProtectedEndpointForAuthenticatedUser behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        @WithMockUser(username = "john@example.com", roles = "USER")
        void shouldAllowAccessToProtectedEndpointForAuthenticatedUser() throws Exception {
            mockMvc.perform(get("/api/customers"))
                    .andExpect(status().isOk());
        }

    /*
     * Test Case: shouldReturnForbiddenWhenUserDoesNotHaveRequiredRole
     * Description: Validates shouldReturnForbiddenWhenUserDoesNotHaveRequiredRole behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        @WithMockUser(username = "john@example.com", roles = "USER")
        void shouldReturnForbiddenWhenUserDoesNotHaveRequiredRole() throws Exception {
            mockMvc.perform(get("/api/accounts/admin-only"))
                    .andExpect(status().isForbidden());
        }

    /*
     * Test Case: shouldAllowAccessWhenAuthorizedRolePresent
     * Description: Validates shouldAllowAccessWhenAuthorizedRolePresent behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        @WithMockUser(username = "admin@example.com", roles = "ADMIN")
        void shouldAllowAccessWhenAuthorizedRolePresent() throws Exception {
            mockMvc.perform(get("/api/accounts/admin-only"))
                    .andExpect(status().isOk());
        }
    }

    /*
     * Test Case: shouldInvokeAuthenticationFilterForBearerTokenRequests
     * Description: Validates shouldInvokeAuthenticationFilterForBearerTokenRequests behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldInvokeAuthenticationFilterForBearerTokenRequests() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .header("Authorization", "Bearer valid.jwt.token"))
                .andExpect(status().isOk());

        verify(jwtFilter).doFilter(any(jakarta.servlet.ServletRequest.class), any(jakarta.servlet.ServletResponse.class),
                any(jakarta.servlet.FilterChain.class));
    }

    /*
     * Test Case: shouldInvokeAuthenticationProviderForValidJwt
     * Description: Validates shouldInvokeAuthenticationProviderForValidJwt behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldInvokeAuthenticationProviderForValidJwt() throws Exception {
        mockMvc.perform(get("/api/accounts")
                        .header("Authorization", "Bearer valid.jwt.token"))
                .andExpect(status().isOk());

        verify(authenticationProvider).authenticate(any(org.springframework.security.core.Authentication.class));
        verifyNoMoreInteractions(authenticationProvider);
    }

    private Object registerRequest() {
        Map<String, Object> map = new HashMap<>(baseRegisterRequest);
        return map;
    }

    private Object loginRequest() {
        Map<String, Object> map = new HashMap<>(baseLoginRequest);
        return map;
    }
}
