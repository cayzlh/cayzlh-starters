package com.cayzlh.framework.jwt.shiro;

import cn.hutool.json.JSONUtil;
import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.jwt.config.ShiroPermissionProperties;
import com.cayzlh.framework.jwt.config.ShiroProperties;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import com.cayzlh.framework.jwt.shiro.exception.ShiroConfigException;
import com.cayzlh.framework.jwt.shiro.service.ShiroLoginService;
import com.cayzlh.framework.util.IniUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({JwtProperties.class})
@ConditionalOnProperty(value = {"cayzlh.framework.jwt.shiro.enable"}, matchIfMissing = true)
public class ShiroConfig {

    /**
     * JWT过滤器名称
     */
    private static final String JWT_FILTER_NAME = "jwtFilter";

    /**
     * Shiro过滤器名称
     */
    private static final String SHIRO_FILTER_NAME = "shiroFilter";

    /**
     * anon
     */
    private static final String ANON = "anon";


    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new JwtCredentialsMatcher();
    }

    /**
     * JWT数据源验证
     */
    @Bean
    public JwtRealm jwtRealm(LoginRedisService loginRedisService) {
        JwtRealm jwtRealm = new JwtRealm(loginRedisService);
        jwtRealm.setCachingEnabled(false);
        jwtRealm.setCredentialsMatcher(credentialsMatcher());
        return jwtRealm;
    }


    @Bean
    public SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean
    public DefaultSubjectDAO subjectDAO() {
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return defaultSubjectDAO;
    }

    /**
     * 安全管理器配置
     */
    @Bean
    public SecurityManager securityManager(LoginRedisService loginRedisService) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm(loginRedisService));
        securityManager.setSubjectDAO(subjectDAO());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean配置
     */
    @Bean(SHIRO_FILTER_NAME)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
            ShiroLoginService shiroLoginService,
            LoginRedisService loginRedisService,
            JwtProperties jwtProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = getFilterMap(shiroLoginService, loginRedisService, jwtProperties);
        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainMap = getFilterChainDefinitionMap(jwtProperties.getShiro());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 获取filter map
     */
    private Map<String, Filter> getFilterMap(ShiroLoginService shiroLoginService,
            LoginRedisService loginRedisService,
            JwtProperties jwtProperties) {
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(JWT_FILTER_NAME, new JwtFilter(shiroLoginService, jwtProperties, loginRedisService));
        return filterMap;
    }


    /**
     * Shiro路径权限配置
     */
    private Map<String, String> getFilterChainDefinitionMap(ShiroProperties shiroProperties) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 获取排除的路径
        List<String[]> anonList = shiroProperties.getAnon();
        log.debug("anonList:{}", JSONUtil.toJsonStr(anonList));
        if (CollectionUtils.isNotEmpty(anonList)) {
            anonList.forEach(anonArray -> {
                if (ArrayUtils.isNotEmpty(anonArray)) {
                    for (String anonPath : anonArray) {
                        filterChainDefinitionMap.put(anonPath, ANON);
                    }
                }
            });
        }

        // 获取ini格式配置
        String definitions = shiroProperties.getFilterChainDefinitions();
        if (StringUtils.isNotBlank(definitions)) {
            Map<String, String> section = IniUtil.parseIni(definitions);
            log.debug("definitions:{}", JSONUtil.toJsonStr(section));
            assert section != null;
            for (Map.Entry<String, String> entry : section.entrySet()) {
                filterChainDefinitionMap.put(entry.getKey(), entry.getValue());
            }
        }

        // 获取自定义权限路径配置集合
        List<ShiroPermissionProperties> permissionConfigs = shiroProperties.getPermission();
        log.debug("permissionConfigs:{}", JSONUtil.toJsonStr(permissionConfigs));
        if (CollectionUtils.isNotEmpty(permissionConfigs)) {
            for (ShiroPermissionProperties permissionConfig : permissionConfigs) {
                String url = permissionConfig.getUrl();
                String[] urls = permissionConfig.getUrls();
                String permission = permissionConfig.getPermission();
                if (StringUtils.isBlank(url) && ArrayUtils.isEmpty(urls)) {
                    throw new ShiroConfigException("shiro permission config 路径配置不能为空");
                }
                if (StringUtils.isBlank(permission)) {
                    throw new ShiroConfigException("shiro permission config permission不能为空");
                }

                if (StringUtils.isNotBlank(url)) {
                    filterChainDefinitionMap.put(url, permission);
                }
                if (ArrayUtils.isNotEmpty(urls)) {
                    for (String string : urls) {
                        filterChainDefinitionMap.put(string, permission);
                    }
                }
            }
        }

        // 如果启用shiro，则设置最后一个设置为JWTFilter，否则全部路径放行
        if (shiroProperties.isEnable()) {
            filterChainDefinitionMap.put("/**", JWT_FILTER_NAME);
        } else {
            filterChainDefinitionMap.put("/**", ANON);
        }

        log.debug("filterChainMap:{}", JSONUtil.toJsonStr(filterChainDefinitionMap));

        // 添加默认的filter
        return addDefaultFilterDefinition(filterChainDefinitionMap);
    }

    /**
     * 添加默认的filter权限过滤
     *
     */
    private Map<String, String> addDefaultFilterDefinition(Map<String, String> filterChainDefinitionMap) {
        if (MapUtils.isEmpty(filterChainDefinitionMap)) {
            return filterChainDefinitionMap;
        }
        final Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : filterChainDefinitionMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String definition;
            String[] strings = value.split(",");
            List<String> list = new ArrayList<>(Arrays.asList(strings));
            definition = String.join(",", list);
            map.put(key, definition);
        }
        return map;
    }

    /**
     * ShiroFilter配置
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER_NAME);
        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return filterRegistrationBean;
    }

    @Bean
    public Authenticator authenticator(LoginRedisService loginRedisService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Collections.singletonList(jwtRealm(loginRedisService)));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }


    /**
     * Enabling Shiro Annotations
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


}
