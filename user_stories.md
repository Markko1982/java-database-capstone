# User Story Template

---

**Title:**  
_As an administrator, I want to log in with my username and password, so that I can securely manage the platform._

**Acceptance Criteria:**  
1. The login form must accept valid credentials and redirect to the dashboard.  
2. Invalid credentials must show an error message.  
3. Password must be encrypted and authenticated securely.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Consider session timeout for inactivity.

---

**Title:**  
_As an administrator, I want to log out from the portal, so that I can protect access to the system._

**Acceptance Criteria:**  
1. A logout button must be visible on the dashboard.  
2. Clicking logout must end the session and redirect to login page.  
3. The session should be cleared on logout.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Should also apply to token/session expiration.

---

**Title:**  
_As an administrator, I want to add doctors to the portal, so that I can keep the staff list updated._

**Acceptance Criteria:**  
1. A form must allow entry of doctor’s details (name, specialty, etc.).  
2. The system must validate and save the new doctor.  
3. The doctor should appear in the list immediately after being added.

**Priority:** High  
**Story Points:** 5  
**Notes:**  
- Consider validation for duplicate entries.

---

**Title:**  
_As an administrator, I want to delete a doctor’s profile, so that I can remove inactive staff from the system._

**Acceptance Criteria:**  
1. A delete option must be available next to each doctor’s profile.  
2. Confirmation dialog must appear before deletion.  
3. The doctor must be removed from the list upon confirmation.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Deletion should be soft (archived), not permanent.

---

**Title:**  
_As an administrator, I want to execute a stored procedure via MySQL CLI, so that I can retrieve monthly consultation stats._

**Acceptance Criteria:**  
1. The procedure must return the number of consultations per month.  
2. The data must be displayed or logged for review.  
3. Error handling must be implemented in case of DB issues.

**Priority:** Low  
**Story Points:** 3  
**Notes:**  
- Procedure should not lock or impact performance.

---

**Title:**  
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**  
1. A public doctor directory must be accessible without authentication.  
2. The list must include name, specialty, and availability.  
3. A prompt to register should be shown when trying to book.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Consider basic filtering (specialty, city).

---

**Title:**  
_As a patient, I want to sign up using my email and password, so that I can book consultations._

**Acceptance Criteria:**  
1. A registration form must collect name, email, password.  
2. Email must be validated and checked for uniqueness.  
3. Password must be stored securely.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Option to receive confirmation email.

---

**Title:**  
_As a patient, I want to log in to the portal, so that I can manage my reservations._

**Acceptance Criteria:**  
1. A login page must authenticate user credentials.  
2. Successful login should redirect to the dashboard.  
3. Incorrect credentials must show an error message.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Add “forgot password” option in future.

---

**Title:**  
_As a patient, I want to log out from the portal, so that I can protect my account._

**Acceptance Criteria:**  
1. Logout option must be clearly visible.  
2. Session must be ended and user redirected to login page.  
3. Cached data should be cleared.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Same behavior across devices.

---

**Title:**  
_As a patient, I want to book a one-hour consultation after logging in, so that I can meet with a doctor._

**Acceptance Criteria:**  
1. Available time slots must be shown after login.  
2. Booking form must confirm date/time and doctor.  
3. Confirmation message must be shown after scheduling.

**Priority:** High  
**Story Points:** 5  
**Notes:**  
- Booking limits (e.g., 1 per day) can be added later.

---

**Title:**  
_As a patient, I want to view my upcoming consultations, so that I can prepare for them properly._

**Acceptance Criteria:**  
1. Future bookings must be listed on the dashboard.  
2. Each entry must include date, time, and doctor name.  
3. Past consultations must not appear.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Include option to cancel if within allowed time window.

- ---

**Title:**  
_As a doctor, I want to log in to the portal, so that I can manage my appointments._

**Acceptance Criteria:**  
1. Login form must authenticate doctor’s credentials.  
2. After login, doctor is redirected to their dashboard.  
3. Incorrect credentials should display a clear error message.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Same login structure as other roles.

---

**Title:**  
_As a doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**  
1. A logout option is always visible on the dashboard.  
2. Logout ends the session and redirects to login screen.  
3. No personal data remains cached.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Apply same security as patient/admin logout.

---

**Title:**  
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**  
1. Dashboard shows a calendar view of upcoming appointments.  
2. Each appointment displays date, time, and patient name.  
3. Calendar can be filtered by week or month.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Integration with patient booking system required.

---

**Title:**  
_As a doctor, I want to mark my unavailability, so that patients can only book during available times._

**Acceptance Criteria:**  
1. Interface allows marking specific times as unavailable.  
2. Unavailable slots are hidden from patient booking view.  
3. Doctors can modify availability anytime.

**Priority:** High  
**Story Points:** 5  
**Notes:**  
- Consider time zone or location differences.

---

**Title:**  
_As a doctor, I want to update my profile with specialization and contact info, so that patients have up-to-date information._

**Acceptance Criteria:**  
1. Form allows editing name, contact, specialty, and description.  
2. Updates are saved and visible to patients instantly.  
3. Validation is applied to all required fields.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Optional field for bio or languages spoken.

---

**Title:**  
_As a doctor, I want to view patient details for upcoming appointments, so that I can prepare in advance._

**Acceptance Criteria:**  
1. Appointment list includes link to patient details.  
2. Patient info includes name, contact, and reason for visit.  
3. Only future appointments are shown.

**Priority:** High  
**Story Points:** 4  
**Notes:**  
- Respect data privacy and access control.


