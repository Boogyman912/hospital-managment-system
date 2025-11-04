<!-- bd651a5b-7b64-4407-bcd8-dc89f3f0ae12 1f0ef487-d41a-4152-8b13-4a800a414e63 -->
# Identify Navigation Conflicts and Redundant Files

## Conflicts Found

### 1. **Dual Navigation Systems**

**Conflict:** `App.jsx` uses state-based navigation while `main.jsx` uses React Router

- **App.jsx (Lines 204-210):**
- Uses `currentView` state to conditionally render components
- Has `handleNavigate` function passed to Header
- Conditionally renders: `<AppointmentBooking />` and `<DoctorManagement />`

- **main.jsx:**
- Uses React Router (`createBrowserRouter`)
- Routes `/doctors` to `DoctorsListPage` (not `DoctorManagement`)
- No route for `AppointmentBooking` component

**Problem:** `App.jsx` Header expects `onNavigate` prop but Header.jsx doesn't accept it (uses React Router `Link` components)

### 2. **Redundant/Unused Files**

**Files that appear redundant:**

1. `src/DoctorManagement.jsx` - Used in App.jsx but not in routing. Admin uses `pages/admin/DoctorsManagement.jsx`
2. `src/AppointmentBooking.jsx` - Used conditionally in App.jsx but booking is handled in `DoctorsListPage.jsx`
3. `src/RegisterDoctor.jsx` - Exists and is routed at `/register-doctor`, but registration is handled in admin panel (`pages/admin/DoctorsManagement.jsx`)

### 3. **Duplicate Login Components**

- `App.jsx` has a `<Login />` section component (lines 173-200)
- `pages/Login.jsx` exists and is routed at `/login`
- Both serve similar purposes but different implementations

### 4. **Header Component Mismatch**

- `App.jsx` passes `onNavigate` prop to Header (line 214)
- `Header.jsx` doesn't accept `onNavigate` - uses React Router `Link` components instead
- This creates a conflict where App.jsx expects callback-based navigation but Header uses router-based navigation

## Recommended Actions

1. **Remove state-based navigation from App.jsx** - Migrate to React Router
2. **Delete or consolidate redundant files:**

- Remove `src/DoctorManagement.jsx` (use admin version)
- Remove `src/AppointmentBooking.jsx` (use DoctorsListPage booking)
- Evaluate if `RegisterDoctor.jsx` is still needed

3. **Fix Header usage** - Remove `onNavigate` prop from App.jsx or update Header to support it
4. **Consolidate Login** - Use only `pages/Login.jsx`, remove Login section from App.jsx

### To-dos

- [ ] Document all navigation conflicts and redundant files
- [ ] Create cleanup plan for removing redundant files and fixing navigation