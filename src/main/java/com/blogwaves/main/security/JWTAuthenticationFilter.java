package com.blogwaves.main.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blogwaves.main.config.ConstantValues;
import com.blogwaves.main.exceptions.InvalidDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

		// Get JWT from request
		// Retrieve the Authorization header
	    String authHeader = request.getHeader("Authorization");
	    String token = null;
	    String username = null;
	    
	    String requestURI = request.getRequestURI();

	    if (isPublicURL(requestURI)) {
	    	
	        filterChain.doFilter(request, response);
	        return;
	    }

	    try {
	    	// Check if the header starts with "Bearer "
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        	
	        	// Extract token
	            token = authHeader.substring(7);
	         // Extract username from token
	            username = jwtTokenHelper.getUserNameFromToken(token);
	        } 
	        else {
	            throw new InvalidDetails("JWT does not begin with Bearer");
	        }

	     // Once we get token , now validate token
			// If the token is valid and no authentication is set in the context
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            
	        	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            
	         // Validate token and set authentication
	            if (jwtTokenHelper.validateToken(token, userDetails)) {
	                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities());
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            } 
	            else {
	                throw new InvalidDetails("Invalid JWT");
	            }
	        }
	        else {
	        	
				throw new InvalidDetails("Username is null or context is not null");
			}
	        
	     // Continue the filter chain
	        filterChain.doFilter(request, response);
	        
	    } catch (ExpiredJwtException e) {
	        handleExceptionResponse(response, HttpStatus.UNAUTHORIZED, "JWT has expired");
	    } catch (MalformedJwtException e) {
	        handleExceptionResponse(response, HttpStatus.UNAUTHORIZED, "Invalid JWT format");
	    } catch (SignatureException e) {
	        handleExceptionResponse(response, HttpStatus.UNAUTHORIZED, "JWT signature is invalid");
	    } catch (InvalidDetails e) {
	        handleExceptionResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
	    } catch (AuthorizationDeniedException e) {
	        handleExceptionResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
	    } 
	    catch (Exception e) {
	        handleExceptionResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
	    }
	}
	
	 /**
     * Utility method to send JSON error responses
     */
    private void handleExceptionResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        
    	response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"status\": " + status.value() + ", \"error\": \"" + status.getReasonPhrase() + "\", \"message\": \"" + message + "\"}");
    }

    /**
    * ✅ Check if the requested URL is public (no JWT validation required)
    */
   private boolean isPublicURL(String requestURI) {
       
	   String[] publicUrls = ConstantValues.PUBLIC_URLS; // Array of permitted URLs
	   
       for (String url : publicUrls) {
    	   String pattern = url.replace("**", ".*"); // Convert ** to regex .* for matching
           
//    	   System.out.println("Checking if " + requestURI + " matches " + url);
    	   if (requestURI.matches(pattern)) {
               return true;
           }
           

       }
       return false;
   }
}
