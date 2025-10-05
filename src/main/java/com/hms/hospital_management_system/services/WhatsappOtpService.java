// package com.hms.hospital_management_system.services;
// import org.apache.hc.client5.http.classic.methods.HttpPost;
// import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
// import org.apache.hc.client5.http.impl.classic.HttpClients;
// import org.apache.hc.core5.http.io.entity.StringEntity;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import java.util.Random;

// @Service
// public class WhatsappOtpService {

//     @Value("${whatsapp.access.token}")
//     private String accessToken;

//     @Value("${whatsapp.phone.id}")
//     private String phoneId;

//     private final Random random = new Random();

//     public String sendOtp(String phoneNumber) throws Exception {
//         int otp = 100000 + random.nextInt(900000);
//         String message = "Your OTP is: " + otp;

//         try (CloseableHttpClient client = HttpClients.createDefault()) {
//             HttpPost request = new HttpPost(
//                 "https://graph.facebook.com/v17.0/" + phoneId + "/messages"
//             );
//             request.setHeader("Authorization", "Bearer " + accessToken);
//             request.setHeader("Content-Type", "application/json");

//             String payload = String.format("""
//             {
//               "messaging_product": "whatsapp",
//               "to": "%s",
//               "type": "text",
//               "text": {"body": "%s"}
//             }
//             """, phoneNumber, message);

//             request.setEntity(new StringEntity(payload));
//             client.execute(request).close();
//         }

//         return String.valueOf(otp); // store in DB or in-memory for verification
//     }
// }
