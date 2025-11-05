import { Link } from "react-router-dom";
import Header from "./components/Header.jsx";
import Footer from "./components/Footer.jsx";

// --- Helper Components & Data ---

const Icon = ({ name, className }) => (
  <svg
    className={`w-6 h-6 ${className}`}
    fill="none"
    stroke="currentColor"
    viewBox="0 0 24 24"
    xmlns="http://www.w3.org/2000/svg"
  >
    {iconPaths[name]}
  </svg>
);

const iconPaths = {
  plus: (
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={2}
      d="M12 4v16m8-8H4"
    />
  ),
  clinic: (
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={2}
      d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0v-4m0 4h5m0 0v-4m0 4h5m0 0v-4m-12-8h10"
    />
  ),
  user: (
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={2}
      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
    />
  ),
  home: (
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={2}
      d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
    />
  ),
};

// --- Section Components ---

const Hero = () => (
  <section
    id="home"
    className="relative text-white h-[60vh] md:h-[80vh] flex items-center bg-cover bg-center"
    style={{
      backgroundImage:
        "url('https://static.vecteezy.com/system/resources/previews/023/740/386/non_2x/medicine-doctor-with-stethoscope-in-hand-on-hospital-background-medical-technology-healthcare-and-medical-concept-photo.jpg')",
    }}
  >
    <div className="absolute inset-0 bg-black opacity-50"></div>
    <div className="container mx-auto px-8 md:px-16 relative z-10">
      <h2 className="text-4xl md:text-6xl font-bold leading-tight">
        Hospital Management
      </h2>
      <p className="text-xl md:text-2xl mt-2">Your Health, Our Priority.</p>
      <p className="mt-4 max-w-lg">
        Comprehensive hospital management solution streamlining patient care, staff operations, and administrative tasks. Experience seamless appointment scheduling, real-time inventory tracking, and integrated billing systems.
      </p>
    </div>
  </section>
);

const Features = () => (
  <section className="bg-gray-900 text-white py-20 px-8 md:px-16">
    <div className="container mx-auto grid md:grid-cols-3 gap-12 text-center">
      {[
        {
          icon: "plus",
          title: "24/7 Patient Care",
          desc: "Round-the-clock medical services with comprehensive patient management, emergency contact tracking, and instant appointment confirmations via WhatsApp and email.",
        },
        {
          icon: "clinic",
          title: "Advanced Facilities",
          desc: "State-of-the-art infrastructure with real-time room availability tracking, advanced lab diagnostics, and automated inventory management for medical supplies.",
        },
        {
          icon: "user",
          title: "Digital Health Records",
          desc: "Secure electronic medical records with complete patient history, prescription tracking, lab results, and integrated billing for seamless healthcare delivery.",
        },
      ].map((f, i) => (
        <div key={i} className="feature-item">
          <div className="flex justify-center mb-4">
            <div className="p-4 bg-gray-800 rounded-full">
              <Icon name={f.icon} className="text-blue-400 w-8 h-8" />
            </div>
          </div>
          <h3 className="text-xl font-semibold mb-2">{f.title}</h3>
          <p className="text-gray-400">{f.desc}</p>
        </div>
      ))}
    </div>
  </section>
);

const AboutUs = () => (
  <section id="about" className="bg-gray-900 text-white py-20 px-8 md:px-16">
    <div className="container mx-auto">
      <div className="flex justify-between items-center mb-8">
        <h2 className="text-3xl md:text-4xl font-bold">About Us</h2>
        <Icon name="home" className="text-gray-500 w-8 h-8" />
      </div>
      <div className="grid md:grid-cols-2 gap-12 text-gray-400">
        <p>
          We have built a smarter way to manage hospital operations and patient
          care. Our platform makes it easy for patients to book online or
          offline appointments without login, receive instant confirmations, and
          get timely reminders via WhatsApp and email.
        </p>
        <p>
          Doctors and staff get powerful dashboards to manage schedules, view
          patient details, record medical history, issue prescriptions, and even
          track inventory — all in one place.
        </p>
        <p>
          By reducing paperwork and streamlining communication, we help
          hospitals run efficiently and give patients a faster, smoother
          healthcare experience.
        </p>
      </div>
    </div>
  </section>
);

const Booking = () => (
  <section id="booking" className="bg-gray-900 text-white py-20 px-8 md:px-16">
    <div className="container mx-auto grid md:grid-cols-2 gap-12 items-center">
      <div>
        <h2 className="text-3xl md:text-4xl font-bold mb-4">
          Book an Appointment
        </h2>
        <p className="text-gray-400 mb-8">
          This section allows patients to view available doctors, select an
          online or offline slot, verify their phone number through OTP, and
          confirm their appointment. After confirmation, the appointment details
          and receipt are generated, and notifications are sent via WhatsApp and
          email.
        </p>
        <Link
          to="/doctors"
          className="inline-block bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-lg transition-colors text-lg"
        >
          Schedule Your Visit
        </Link>
      </div>
      <div className="flex justify-center">
        <img
          src="https://placehold.co/600x400/e2e8f0/4a5568?text=Doctor+%26+Patient"
          alt="Doctor with patient"
          className="rounded-lg shadow-2xl"
        />
      </div>
    </div>
  </section>
);

// --- Main App Component ---

export default function App() {
  return (
    <div className="bg-gray-900 font-sans">
      <Header />
      <main>
        <Hero />
        <Features />
        <AboutUs />
        <Booking />
      </main>
      <Footer />
    </div>
  );
}