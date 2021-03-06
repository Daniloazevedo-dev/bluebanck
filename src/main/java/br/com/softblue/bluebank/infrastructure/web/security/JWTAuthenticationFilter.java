package br.com.softblue.bluebank.infrastructure.web.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.softblue.bluebank.domain.usuario.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			ObjectMapper mapper = new ObjectMapper();
			Usuario usuario = mapper.readValue(request.getInputStream(), Usuario.class);

			UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(usuario.getEmail(),
					usuario.getSenha());

			return authenticationManager.authenticate(upat);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authResult.getPrincipal();

		String jwtToken = Jwts.builder().setSubject(userDetailsImpl.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.claim("displayName", userDetailsImpl.getDisplayname()).claim("cpf", userDetailsImpl.getCpf())
				.claim("contasBancarias", userDetailsImpl.getContasBancarias())
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY).compact();

		response.addHeader(SecurityConstants.AUTHORIZATION_HEADER, SecurityConstants.TOKEN_PREFIX + jwtToken);

	}
}
