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

