package ca.sheridancollege.waamande.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
		
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		
		return jdbcUserDetailsManager;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**").and().ignoring().antMatchers("/h2-console/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests().antMatchers("/registerDoctor").permitAll()
		.and()
		.authorizeRequests().antMatchers("/registerPatient").permitAll()
		.and()
		.authorizeRequests().antMatchers("/").hasAnyRole("DOCTOR", "PATIENT")
		.and()
		.authorizeRequests().antMatchers("/searchDoctors").hasRole("PATIENT")
		.and()
		.authorizeRequests().antMatchers("/requestAppointment").hasRole("PATIENT")
		.and()
		.authorizeRequests().antMatchers("/requests").hasRole("DOCTOR")
		.and()
		.authorizeRequests().antMatchers("/accept/{id}").hasRole("DOCTOR")
		.and()
		.authorizeRequests().antMatchers("/decline/{id}").hasRole("DOCTOR")
		.and()
		.authorizeRequests().antMatchers("/studentAppointments").hasRole("PATIENT")
		.and()
		.authorizeRequests().antMatchers("/describeDoctor").hasRole("PATIENT")
		.and()
		.authorizeRequests().antMatchers("/describeDoctors").hasRole("PATIENT")
		.and()
		.authorizeRequests().antMatchers("/images/**").permitAll()
		.and()
		.authorizeRequests().antMatchers("/javascript/**").permitAll()
		.and()
		.authorizeRequests().antMatchers("/css/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").permitAll()
		.and()
		.logout().invalidateHttpSession(true).clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login?logout").permitAll();
	}

}
