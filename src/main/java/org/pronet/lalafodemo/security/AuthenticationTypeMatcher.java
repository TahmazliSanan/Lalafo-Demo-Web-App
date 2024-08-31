package org.pronet.lalafodemo.security;

public class AuthenticationTypeMatcher {
    public static final String[] NON_AUTH_ROUTES = {
            "/category-images/**", "/product-images/**", "/customer-images/**",
            "/",
            "/auth/registration-view", "/auth/register", "/auth/login-view",
            "/category/list-for-user",
            "/product/list"
    };

    public static final String[] ADMIN_AUTH_ROUTES = {
            "/admin/**",
            "/category/create-view", "/category/create",
            "/category/list-for-admin",
            "/category/update-view/{id}", "/category/update",
            "/category/delete-view/{id}", "/category/delete"
    };

    public static final String[] CUSTOMER_AUTH_ROUTES = {
            "/category/{id}/product-list",
            "/product/create-view", "/product/create",
            "/product/list/my-products",
            "/product/details/{id}",
            "/product/update-view/{id}", "/product/update",
            "/product/delete-view/{id}", "/product/delete",
            "/auth/delete-view", "/auth/delete"
    };

    public static final String[] ADMIN_AND_CUSTOMER_AUTH_ROUTES = {
            "/profile/**"
    };
}
