/**
 * API Endpoints:
 * - POST /api/users/register -> Registers a new user
 * - POST /api/users/login -> Logs in an existing user
 * - POST /api/users/reset-password -> Sends a password reset link
 */

const toggleForm = document.getElementById('toggle-form');
const formTitle = document.getElementById('form-title');
const extraFields = document.getElementById('extra-fields');
const authForm = document.getElementById('auth-form');
const forgotPasswordContainer = document.getElementById('forgot-password-container'); 
const resetForm = document.getElementById('reset-form');
const backToLogin = document.getElementById('back-to-login');

toggleForm.addEventListener('click', (e) => {
    e.preventDefault();
    if (formTitle.innerText === 'Login') {
        formTitle.innerText = 'Register';
        extraFields.classList.remove('hidden');
        extraFields.style.display = 'block';
        toggleForm.innerText = 'Already have an account? Login';
        forgotPasswordContainer.style.display = 'none';
    } else {
        formTitle.innerText = 'Login';
        extraFields.classList.remove('hidden');
        toggleForm.innerText = "Don't have an account? Register";
        forgotPasswordContainer.classList.remove('hidden');
        forgotPasswordContainer.style.display = 'block';
    }
});
forgotPasswordContainer.addEventListener('click', (e) => {
    e.preventDefault();
    authForm.classList.add('hidden');
    resetForm.classList.remove('hidden');
});

backToLogin.addEventListener('click', (e) => {
    e.preventDefault();
    resetForm.classList.add('hidden');
    authForm.classList.remove('hidden');
});

// Handle form submission for login and registration
authForm.addEventListener("submit", async (event) => {
    event.preventDefault(); // Prevent default form submission

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const name = document.getElementById("name")?.value || "";
    const country = document.getElementById("country")?.value || "";

    // Determine if it's a login or registration based on form title
    const isRegistering = formTitle.innerText === "Register";
    const endpoint = isRegistering ? "/api/users/register" : "/api/users/login";

    const userData = isRegistering
        ? { name, email, password, country } // For registration
        : { email, password }; // For login

    try {
        const response = await fetch(`http://localhost:8080${endpoint}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData),
        });

        if (!response.ok) {
            throw new Error("Failed to submit form");
        }

        const data = await response.json();
        alert(isRegistering ? "Registration successful!" : "Login successful!");
        console.log("Server Response:", data);

        // Redirect after successful login/registration
        if (!isRegistering) {
            window.location.href = "/dashboard.html"; // Change as needed
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Something went wrong. Please try again.");
    }
});

// Handle password reset form
resetForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const resetEmail = document.getElementById("reset-email").value;

    try {
        const response = await fetch("http://localhost:8080/api/users/reset-password", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: resetEmail }),
        });

        if (!response.ok) {
            throw new Error("Failed to send reset link");
        }

        alert("Password reset link sent to your email.");
        resetForm.classList.add("hidden");
        authForm.classList.remove("hidden");
    } catch (error) {
        console.error("Error:", error);
        alert("Failed to send password reset link.");
    }
});
