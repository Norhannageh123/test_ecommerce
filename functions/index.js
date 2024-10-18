const functions = require("firebase-functions");
const admin = require("firebase-admin");
const cors = require("cors")({origin: true});
const nodemailer = require("nodemailer"); // تأكد من تثبيت Nodemailer

admin.initializeApp();

// قم بحفظ بيانات الدخول في Firebase Environment Variables
const gmailUser = functions.config().gmail.user;
const gmailPass = functions.config().gmail.pass;

exports.sendVerificationEmail = functions.https.onRequest((req, res) => {
  cors(req, res, async () => {
    if (req.method !== "POST") {
      return res.status(405).send("Method Not Allowed");
    }

    const email = req.body.email;
    const verificationCode = Math.floor(1000 + Math.random() * 9000).toString();

    const mailOptions = {
      from: gmailUser,
      to: email,
      subject: "Verification Code",
      text: `Your verification code is: ${verificationCode}`,
    };

    try {
      const userRecord = await admin.auth().getUserByEmail(email);
      if (userRecord) {
        // إنشاء Nodemailer transporter
        const transporter = nodemailer.createTransport({
          service: "gmail",
          auth: {
            user: gmailUser,
            pass: gmailPass,
          },
        });

        // إرسال البريد الإلكتروني
        await transporter.sendMail(mailOptions);
        return res.status(200).send(`Verification email sent to ${email}`);
      }
    } catch (error) {
      console.error(`Error fetching user by email: ${email}`, error);
      return res.status(404).send("Email not found.");
    }
  });
});
