package io.shaka.http


object TestCerts {
  // For server

  // $ keytool -list -keystore keystore-testing.jks -storepass password
  // naive-testing, 16-Jul-2014, PrivateKeyEntry,
  // Certificate fingerprint (MD5): 90:75:3A:10:1C:64:18:86:B4:EC:36:2D:40:2F:69:F6
  val keyStoreWithServerCert: PathAndPassword = PathAndPassword("src/test/resources/certs/keystore-testing.jks", "password")

  // $ keytool -list -keystore server-truststore.jks -storepass password
  // [...]
  // naive-testing-client, 10-Jun-2015, trustedCertEntry,
  // Certificate fingerprint (MD5): DC:22:BD:9D:C4:7D:3E:A8:80:3E:BF:69:D9:8B:03:20
  val trustStoreWithClientCert: PathAndPassword = PathAndPassword("src/test/resources/certs/server-truststore.jks", "password")


  // For client

  // $ keytool -list -keystore client-truststore.jks -storepass password
  // [...]
  // naive-testing-server, 11-Jun-2015, trustedCertEntry,
  // Certificate fingerprint (MD5): 90:75:3A:10:1C:64:18:86:B4:EC:36:2D:40:2F:69:F6
  val trustStoreWithServerCert: PathAndPassword = PathAndPassword("src/test/resources/certs/client-truststore.jks", "password")

  // $ keytool -list -keystore keystore-testing-client.jks -storepass password
  // [...]
  // naive-testing-client, 10-Jun-2015, PrivateKeyEntry,
  // Certificate fingerprint (MD5): DC:22:BD:9D:C4:7D:3E:A8:80:3E:BF:69:D9:8B:03:20
  val keyStoreWithClientCert: PathAndPassword = PathAndPassword("src/test/resources/certs/keystore-testing-client.jks", "password")
}