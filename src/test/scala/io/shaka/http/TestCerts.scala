package io.shaka.http


object TestCerts {
  def cert(filename: String) = "src/test/resources/certs/" + filename
  // For server

  // $ keytool -list -keystore keystore-testing.jks -storepass password
  // naive-testing, 16-Jul-2014, PrivateKeyEntry,
  // Certificate fingerprint (MD5): 90:75:3A:10:1C:64:18:86:B4:EC:36:2D:40:2F:69:F6
  val keyStoreWithServerCert: PathAndPassword = PathAndPassword(cert("keystore-testing.jks"), "password")

  // $ keytool -list -keystore server-truststore.jks -storepass password
  // [...]
  // naive-testing-client, 10-Jun-2015, trustedCertEntry,
  // Certificate fingerprint (MD5): DC:22:BD:9D:C4:7D:3E:A8:80:3E:BF:69:D9:8B:03:20
  val trustStoreWithClientCert: PathAndPassword = PathAndPassword(cert("server-truststore.jks"), "password")


  // For client

  // $ keytool -list -keystore client-truststore.jks -storepass password
  // [...]
  // naive-testing-server, 11-Jun-2015, trustedCertEntry,
  // Certificate fingerprint (MD5): 90:75:3A:10:1C:64:18:86:B4:EC:36:2D:40:2F:69:F6
  val trustStoreWithServerCert: PathAndPassword = PathAndPassword(cert("client-truststore.jks"), "password")

  // $ keytool -list -keystore keystore-testing-client.jks -storepass password
  // [...]
  // naive-testing-client, 10-Jun-2015, PrivateKeyEntry,
  // Certificate fingerprint (MD5): DC:22:BD:9D:C4:7D:3E:A8:80:3E:BF:69:D9:8B:03:20
  val keyStoreWithClientCert: PathAndPassword = PathAndPassword(cert("keystore-testing-client.jks"), "password")

  // $ keytool -list -keystore client-certificate-unrecognised-by-server.jks -storepass password
  // [...]
  // unrecognised client certificate, 09-Jul-2015, PrivateKeyEntry,
  // Certificate fingerprint (MD5): 9F:B7:00:8B:80:3B:55:64:A7:30:D8:8F:1C:A7:E3:59
  val keyStoreWithClientCertUnrecognisedByServer: PathAndPassword = PathAndPassword(cert("client-certificate-unrecognised-by-server.jks"), "password")
}