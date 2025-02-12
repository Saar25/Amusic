package org.saartako.encrypt;


import com.auth0.jwt.algorithms.Algorithm;

public interface JwtParser<T> {

    String sign(Algorithm algorithm, T object);

    T verify(Algorithm algorithm, String token);

    T parse(String token);
}