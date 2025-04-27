// import React from 'react';
// import { GoogleLogin } from '@react-oauth/google';
// //import { jwtDecode } from 'jwt-decode';
// import { useNavigate } from 'react-router-dom'; // Redirect after login
//
// function GoogleSignIn() {
//   const navigate = useNavigate(); // initialize navigate
//
//   const handleSuccess = async (credentialResponse) => {
//     console.log('Login Success:', credentialResponse);
//
//     try {
//       const res = await fetch('http://localhost:8000/api/v1/auth/google-login', {
//         method: 'POST',
//         headers: {
//           'Content-Type': 'application/json',
//         },
//         body: JSON.stringify({
//           token: credentialResponse.credential,
//         }),
//       });
//
//       if (!res.ok) {
//         throw new Error('Failed to login with Google');
//       }
//
//       const data = await res.json();
//       console.log('Server Response:', data);
//
//       // Save token and user info
//       localStorage.setItem('token', data.token);
//       localStorage.setItem('fullName', data.data.fullName);
//
//       // Redirect user after login
//       navigate('/dashboard'); // or any other page
//     } catch (error) {
//       console.error('Error during Google login:', error);
//     }
//   };
//
//   const handleError = () => {
//     console.error('Google Login Failed');
//   };
//
//   return (
//     <div style={{ marginTop: '50px', textAlign: 'center' }}>
//       <h2>Login with Google</h2>
//       <GoogleLogin
//         onSuccess={handleSuccess}
//         onError={handleError}
//         useOneTap={false}
//       />
//     </div>
//   );
// }
//
// export default GoogleSignIn;
