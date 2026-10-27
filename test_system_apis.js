// Comprehensive End-to-End API Test Suite for Blood Bank Management System

const BASE_URL = 'http://localhost:8080/api';

async function runTests() {
  console.log('================================================================');
  console.log('   BLOOD BANK MANAGEMENT SYSTEM - FULL API VERIFICATION SUITE   ');
  console.log('================================================================\n');

  let passed = 0;
  let failed = 0;

  function assert(condition, testName, extra = '') {
    if (condition) {
      console.log(`[PASS] ${testName} ${extra}`);
      passed++;
    } else {
      console.error(`[FAIL] ${testName} ${extra}`);
      failed++;
    }
  }

  try {
    // 1. Public Search - All available donors
    console.log('--- TEST 1: Public Search (All Available Donors) ---');
    const searchRes = await fetch(`${BASE_URL}/donors/search`);
    const searchData = await searchRes.json();
    assert(searchRes.status === 200, 'Search returns 200 OK');
    assert(searchData.success === true, 'Response indicates success = true');
    assert(Array.isArray(searchData.data) && searchData.data.length > 0, `Returns seeded donors (count: ${searchData.data?.length})`);
    
    // Check privacy: ensure email & password are NOT in public search
    const firstDonor = searchData.data[0];
    assert(!firstDonor.email, 'Public search hides donor email for privacy');
    assert(!firstDonor.password && !firstDonor.passwordHash, 'Public search hides password');
    assert(firstDonor.availability === 'YES', 'Only availability = YES donors returned in public search');

    // 2. Public Search by Blood Group & City
    console.log('\n--- TEST 2: Filter by Blood Group and City ---');
    const filterRes = await fetch(`${BASE_URL}/donors/search?bloodGroup=O%2B&city=Chennai`);
    const filterData = await filterRes.json();
    assert(filterRes.status === 200, 'Filter search returns 200 OK');
    assert(filterData.data.every(d => d.bloodGroup === 'O+'), 'All results match bloodGroup O+');

    // 3. Donor Registration
    console.log('\n--- TEST 3: Donor Registration ---');
    const uniquePhone = '9988776655';
    const uniqueEmail = 'testdonor@bloodbank.org';
    const regPayload = {
      name: 'Dr. Vikram Seth',
      age: 28,
      bloodGroup: 'B+',
      city: 'Coimbatore',
      phone: uniquePhone,
      email: uniqueEmail,
      password: 'password123',
      confirmPassword: 'password123'
    };

    const regRes = await fetch(`${BASE_URL}/donors/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(regPayload)
    });
    const regData = await regRes.json();
    assert(regRes.status === 201, 'Registration returns 201 Created');
    assert(regData.data.phone === uniquePhone, 'Registered phone matches');
    assert(regData.data.availability === 'YES', 'Default availability is YES');

    // 4. Duplicate Registration Validation
    console.log('\n--- TEST 4: Duplicate Registration Validation ---');
    const dupRes = await fetch(`${BASE_URL}/donors/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(regPayload)
    });
    const dupData = await dupRes.json();
    assert(dupRes.status === 400 || dupRes.status === 409, 'Duplicate registration correctly rejected (status 400/409)');
    assert(dupData.success === false, 'Duplicate response success = false');

    // 5. Donor Login
    console.log('\n--- TEST 5: Donor Login ---');
    const loginRes = await fetch(`${BASE_URL}/auth/donor/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ phone: uniquePhone, password: 'password123' })
    });
    const loginData = await loginRes.json();
    assert(loginRes.status === 200, 'Donor login returns 200 OK');
    assert(!!loginData.data.token, 'JWT token generated in response');
    const donorToken = loginData.data.token;

    // 6. Donor View Profile (Protected)
    console.log('\n--- TEST 6: Protected GET /api/donors/me ---');
    const profileRes = await fetch(`${BASE_URL}/donors/me`, {
      headers: { 'Authorization': `Bearer ${donorToken}` }
    });
    const profileData = await profileRes.json();
    assert(profileRes.status === 200, 'View profile returns 200 OK');
    assert(profileData.data.name === 'Dr. Vikram Seth', 'Profile name matches');

    // 7. Donor Update Profile (Protected)
    console.log('\n--- TEST 7: Protected PUT /api/donors/me ---');
    const updateRes = await fetch(`${BASE_URL}/donors/me`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${donorToken}`
      },
      body: JSON.stringify({
        name: 'Dr. Vikram Seth Updated',
        age: 29,
        bloodGroup: 'B+',
        city: 'Tiruppur',
        email: uniqueEmail
      })
    });
    const updateData = await updateRes.json();
    assert(updateRes.status === 200, 'Update profile returns 200 OK');
    assert(updateData.data.name === 'Dr. Vikram Seth Updated', 'Updated name reflected');
    assert(updateData.data.city === 'Tiruppur', 'Updated city reflected');

    // 8. Donor Availability Toggle (Protected)
    console.log('\n--- TEST 8: Protected PATCH /api/donors/me/availability ---');
    const availRes = await fetch(`${BASE_URL}/donors/me/availability`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${donorToken}`
      },
      body: JSON.stringify({ availability: 'NO' })
    });
    const availData = await availRes.json();
    assert(availRes.status === 200, 'Availability update returns 200 OK');
    assert(availData.data.availability === 'NO', 'Availability status changed to NO');

    // 9. Verify Unavailable Donor is Excluded from Public Search
    console.log('\n--- TEST 9: Verify Excluded from Public Search when NO ---');
    const reSearchRes = await fetch(`${BASE_URL}/donors/search?city=Tiruppur`);
    const reSearchData = await reSearchRes.json();
    const foundInSearch = reSearchData.data.some(d => d.phone === uniquePhone);
    assert(!foundInSearch, 'Donor with availability = NO is excluded from public search');

    // 10. Admin Login
    console.log('\n--- TEST 10: Admin Login ---');
    const adminLoginRes = await fetch(`${BASE_URL}/auth/admin/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: 'admin', password: 'admin123' })
    });
    const adminLoginData = await adminLoginRes.json();
    assert(adminLoginRes.status === 200, 'Admin login returns 200 OK');
    assert(adminLoginData.data.role === 'ROLE_ADMIN', 'Role is ROLE_ADMIN');
    const adminToken = adminLoginData.data.token;

    // 11. Admin Get Donors
    console.log('\n--- TEST 11: Protected GET /api/admin/donors ---');
    const adminDonorsRes = await fetch(`${BASE_URL}/admin/donors`, {
      headers: { 'Authorization': `Bearer ${adminToken}` }
    });
    const adminDonorsData = await adminDonorsRes.json();
    assert(adminDonorsRes.status === 200, 'Admin get donors returns 200 OK');
    assert(Array.isArray(adminDonorsData.data) && adminDonorsData.data.length >= 9, `Total donors listed: ${adminDonorsData.data.length}`);

    // 12. Admin Get Statistics
    console.log('\n--- TEST 12: Protected GET /api/admin/donors/statistics ---');
    const statsRes = await fetch(`${BASE_URL}/admin/donors/statistics`, {
      headers: { 'Authorization': `Bearer ${adminToken}` }
    });
    const statsData = await statsRes.json();
    assert(statsRes.status === 200, 'Admin statistics returns 200 OK');
    assert(statsData.data.totalDonors >= 9, `Total donors count = ${statsData.data.totalDonors}`);
    assert(typeof statsData.data.availableDonors === 'number', `Available donors = ${statsData.data.availableDonors}`);
    assert(typeof statsData.data.unavailableDonors === 'number', `Unavailable donors = ${statsData.data.unavailableDonors}`);
    assert(statsData.data.bloodGroupCounts && typeof statsData.data.bloodGroupCounts['O+'] === 'number', 'Blood group breakdown present');

    // 13. Admin Delete Donor
    console.log('\n--- TEST 13: Protected DELETE /api/admin/donors/{id} ---');
    const createdDonorId = regData.data.id;
    const deleteRes = await fetch(`${BASE_URL}/admin/donors/${createdDonorId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${adminToken}` }
    });
    const deleteData = await deleteRes.json();
    assert(deleteRes.status === 200, 'Admin delete returns 200 OK');
    assert(deleteData.success === true, 'Delete success = true');

    // Verify deletion in database
    const verifyDelRes = await fetch(`${BASE_URL}/admin/donors`, {
      headers: { 'Authorization': `Bearer ${adminToken}` }
    });
    const verifyDelData = await verifyDelRes.json();
    const stillExists = verifyDelData.data.some(d => d.id === createdDonorId);
    assert(!stillExists, 'Donor successfully removed from MongoDB collection');

    // 14. Forgot Password & OTP Flow
    console.log('\n--- TEST 14: Forgot Password & OTP Flow ---');
    const forgotRes = await fetch(`${BASE_URL}/auth/forgot-password`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ identifier: '9876543210' }) // Rahul Sharma seeded donor
    });
    const forgotData = await forgotRes.json();
    assert(forgotRes.status === 200, 'Forgot password request returns 200 OK');
    assert(forgotData.success === true, 'Forgot password response indicates OTP dispatched');

    console.log('\n================================================================');
    console.log(`   TEST RESULTS: ${passed} PASSED, ${failed} FAILED   `);
    console.log('================================================================');

  } catch (error) {
    console.error('Test execution error:', error);
  }
}

runTests();
