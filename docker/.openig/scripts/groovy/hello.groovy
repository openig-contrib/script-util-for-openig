/**
 * Access to org.forgerock.* libs is mandatory to be able to decrypt the secret.
 */
import static java.util.concurrent.TimeUnit.SECONDS

import org.forgerock.secrets.*
import org.forgerock.util.Function

def myRevealedSecret = secretService.getNamedSecret(Purpose.PASSWORD, 'mysecret')
                                    .then(new Function<GenericSecret, String, NoSuchSecretException>() {
    @Override
    public String apply(final GenericSecret secret) throws NoSuchSecretException {
        return secret.revealAsUtf8(new Function<char[], String, Exception>() {
            @Override
            public String apply(final char[] chars) throws Exception {
                return new String(chars);
            }
        })}
}).getOrThrow(30, SECONDS);
entity = """<html><body><p>Hello ${myRevealedSecret} !!!</p></body></html>""" as String
response = new Response(Status.OK)
response.entity = entity
return response
