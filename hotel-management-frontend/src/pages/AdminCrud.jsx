import React, { useState, useEffect } from 'react';
import API from '../services/api';

const AdminCrud = () => {
  const [activeTab, setActiveTab] = useState('rooms');
  
  // Data lists
  const [rooms, setRooms] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [services, setServices] = useState([]);
  const [invoices, setInvoices] = useState([]);

  // Form states
  const [roomForm, setRoomForm] = useState({ roomNumber: '', floor: '', status: 'VACANT_CLEAN', roomTypeId: 1 });
  const [employeeForm, setEmployeeForm] = useState({ employeeCode: '', fullName: '', position: '', email: '', password: '', phoneNumber: '', salaryRate: '', roleId: 2 });
  const [serviceForm, setServiceForm] = useState({ name: '', description: '', price: '', unit: 'Lượt' });

  // Response Feedback
  const [feedback, setFeedback] = useState('');
  const [errorMsg, setErrorMsg] = useState('');

  useEffect(() => {
    loadTabDetails(activeTab);
  }, [activeTab]);

  const loadTabDetails = async (tab) => {
    setFeedback('');
    setErrorMsg('');
    try {
      if (tab === 'rooms') {
        const res = await API.get('/rooms');
        setRooms(res.data.data);
      } else if (tab === 'employees') {
        const res = await API.get('/employees');
        setEmployees(res.data.data);
      } else if (tab === 'services') {
        const res = await API.get('/services');
        setServices(res.data.data);
      } else if (tab === 'invoices') {
        const res = await API.get('/invoices');
        setInvoices(res.data.data);
      }
    } catch (err) {
      setErrorMsg('Failed to query tab details. Verify admin credentials.');
    }
  };

  const handleRoomCreate = async (e) => {
    e.preventDefault();
    try {
      await API.post('/rooms', roomForm);
      setFeedback('Room created successfully.');
      setRoomForm({ roomNumber: '', floor: '', status: 'VACANT_CLEAN', roomTypeId: 1 });
      loadTabDetails('rooms');
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Error occurred.');
    }
  };

  const handleEmployeeCreate = async (e) => {
    e.preventDefault();
    try {
      await API.post('/employees', employeeForm);
      setFeedback('Employee created successfully.');
      setEmployeeForm({ employeeCode: '', fullName: '', position: '', email: '', password: '', phoneNumber: '', salaryRate: '', roleId: 2 });
      loadTabDetails('employees');
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Error occurred.');
    }
  };

  const handleServiceCreate = async (e) => {
    e.preventDefault();
    try {
      await API.post('/services', serviceForm);
      setFeedback('Service created successfully.');
      setServiceForm({ name: '', description: '', price: '', unit: 'Lượt' });
      loadTabDetails('services');
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Error occurred.');
    }
  };

  const handleItemDelete = async (endpoint, id, refetchTab) => {
    if (!window.confirm('Confirm delete this record?')) return;
    try {
      await API.delete(`${endpoint}/${id}`);
      setFeedback('Record deleted successfully.');
      loadTabDetails(refetchTab);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Could not delete record.');
    }
  };

  return (
    <div className="container py-5">
      <div className="mb-4">
        <h1 className="fw-bold">⚙️ Admin Control Panel</h1>
        <p className="text-muted">Perform raw CRUD operations for hotel entities here.</p>
      </div>

      {feedback && <div className="alert alert-success border-0">{feedback}</div>}
      {errorMsg && <div className="alert alert-danger border-0">{errorMsg}</div>}

      {/* Tabs Menu */}
      <ul className="nav nav-pills mb-4 gap-2 bg-light p-2 rounded-4 border">
        <li className="nav-item">
          <button className={`nav-link px-4 py-2 ${activeTab === 'rooms' ? 'active btn-primary-gradient' : 'text-dark'}`} onClick={() => setActiveTab('rooms')}>Rooms</button>
        </li>
        <li className="nav-item">
          <button className={`nav-link px-4 py-2 ${activeTab === 'employees' ? 'active btn-primary-gradient' : 'text-dark'}`} onClick={() => setActiveTab('employees')}>Employees</button>
        </li>
        <li className="nav-item">
          <button className={`nav-link px-4 py-2 ${activeTab === 'services' ? 'active btn-primary-gradient' : 'text-dark'}`} onClick={() => setActiveTab('services')}>Services</button>
        </li>
        <li className="nav-item">
          <button className={`nav-link px-4 py-2 ${activeTab === 'invoices' ? 'active btn-primary-gradient' : 'text-dark'}`} onClick={() => setActiveTab('invoices')}>Invoices</button>
        </li>
      </ul>

      {/* Rooms Tab */}
      {activeTab === 'rooms' && (
        <div className="row g-4">
          <div className="col-lg-8">
            <div className="card glass-card p-4 border-0 shadow-sm">
              <h5 className="fw-bold mb-3">Room Catalog Inventory</h5>
              <div className="table-responsive">
                <table className="table align-middle">
                  <thead>
                    <tr>
                      <th>RoomNo</th>
                      <th>Type</th>
                      <th>Floor</th>
                      <th>Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rooms.map(r => (
                      <tr key={r.roomId}>
                        <td><strong>Room {r.roomNumber}</strong></td>
                        <td>{r.roomType.name}</td>
                        <td>Floor {r.floor}</td>
                        <td><span className="badge bg-secondary">{r.status}</span></td>
                        <td>
                          <button className="btn btn-outline-danger btn-sm rounded-3" onClick={() => handleItemDelete('/rooms', r.roomId, 'rooms')}>Delete</button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <div className="col-lg-4">
            <div className="card glass-card p-4 border-0 shadow-sm">
              <h5 className="fw-bold mb-3">Create Room</h5>
              <form onSubmit={handleRoomCreate}>
                <div className="mb-3">
                  <label className="form-label">Room Number</label>
                  <input type="text" className="form-control" value={roomForm.roomNumber} onChange={e => setRoomForm({...roomForm, roomNumber: e.target.value})} required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Floor</label>
                  <input type="number" className="form-control" value={roomForm.floor} onChange={e => setRoomForm({...roomForm, floor: e.target.value})} required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Room Type ID</label>
                  <input type="number" className="form-control" value={roomForm.roomTypeId} onChange={e => setRoomForm({...roomForm, roomTypeId: parseInt(e.target.value)})} required />
                </div>
                <button type="submit" className="btn btn-primary-gradient w-100 mt-2">Submit</button>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Employees Tab */}
      {activeTab === 'employees' && (
        <div className="row g-4">
          <div className="col-lg-8">
            <div className="card glass-card p-4 border-0 shadow-sm">
              <h5 className="fw-bold mb-3">Hotel Staff List</h5>
              <div className="table-responsive">
                <table className="table align-middle">
                  <thead>
                    <tr>
                      <th>Code</th>
                      <th>Name</th>
                      <th>Position</th>
                      <th>Salary Rate</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {employees.map(e => (
                      <tr key={e.employeeId}>
                        <td><strong>{e.employeeCode}</strong></td>
                        <td>{e.fullName}</td>
                        <td>{e.position}</td>
                        <td>${e.salaryRate}</td>
                        <td>
                          <button className="btn btn-outline-danger btn-sm rounded-3" onClick={() => handleItemDelete('/employees', e.employeeId, 'employees')}>Delete</button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <div className="col-lg-4">
            <div className="card glass-card p-4 border-0 shadow-sm">
              <h5 className="fw-bold mb-3">Add Employee</h5>
              <form onSubmit={handleEmployeeCreate}>
                <div className="mb-2">
                  <label className="form-label">Code</label>
                  <input type="text" className="form-control form-control-sm" value={employeeForm.employeeCode} onChange={e => setEmployeeForm({...employeeForm, employeeCode: e.target.value})} required />
                </div>
                <div className="mb-2">
                  <label className="form-label">Full Name</label>
                  <input type="text" className="form-control form-control-sm" value={employeeForm.fullName} onChange={e => setEmployeeForm({...employeeForm, fullName: e.target.value})} required />
                </div>
                <div className="mb-2">
                  <label className="form-label">Position</label>
                  <input type="text" className="form-control form-control-sm" value={employeeForm.position} onChange={e => setEmployeeForm({...employeeForm, position: e.target.value})} required />
                </div>
                <div className="mb-2">
                  <label className="form-label">Email</label>
                  <input type="email" className="form-control form-control-sm" value={employeeForm.email} onChange={e => setEmployeeForm({...employeeForm, email: e.target.value})} required />
                </div>
                <div className="mb-2">
                  <label className="form-label">Password</label>
                  <input type="password" className="form-control form-control-sm" value={employeeForm.password} onChange={e => setEmployeeForm({...employeeForm, password: e.target.value})} required />
                </div>
                <div className="mb-2">
                  <label className="form-label">Phone</label>
                  <input type="text" className="form-control form-control-sm" value={employeeForm.phoneNumber} onChange={e => setEmployeeForm({...employeeForm, phoneNumber: e.target.value})} required />
                </div>
                <div className="mb-2">
                  <label className="form-label">Salary Rate</label>
                  <input type="number" className="form-control form-control-sm" value={employeeForm.salaryRate} onChange={e => setEmployeeForm({...employeeForm, salaryRate: e.target.value})} required />
                </div>
                <button type="submit" className="btn btn-primary-gradient w-100 mt-2">Submit</button>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Services Tab */}
      {activeTab === 'services' && (
        <div className="row g-4">
          <div className="col-lg-8">
            <div className="card glass-card p-4 border-0 shadow-sm">
              <h5 className="fw-bold mb-3">Custom Hotel Services</h5>
              <div className="table-responsive">
                <table className="table align-middle">
                  <thead>
                    <tr>
                      <th>Service Name</th>
                      <th>Price</th>
                      <th>Unit</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {services.map(s => (
                      <tr key={s.serviceId}>
                        <td><strong>{s.name}</strong></td>
                        <td>${s.price}</td>
                        <td>{s.unit}</td>
                        <td>
                          <button className="btn btn-outline-danger btn-sm rounded-3" onClick={() => handleItemDelete('/services', s.serviceId, 'services')}>Delete</button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <div className="col-lg-4">
            <div className="card glass-card p-4 border-0 shadow-sm">
              <h5 className="fw-bold mb-3">Add Service</h5>
              <form onSubmit={handleServiceCreate}>
                <div className="mb-3">
                  <label className="form-label">Service Name</label>
                  <input type="text" className="form-control" value={serviceForm.name} onChange={e => setServiceForm({...serviceForm, name: e.target.value})} required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Price</label>
                  <input type="number" className="form-control" value={serviceForm.price} onChange={e => setServiceForm({...serviceForm, price: e.target.value})} required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Unit</label>
                  <input type="text" className="form-control" value={serviceForm.unit} onChange={e => setServiceForm({...serviceForm, unit: e.target.value})} required />
                </div>
                <button type="submit" className="btn btn-primary-gradient w-100 mt-2">Submit</button>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Invoices Tab */}
      {activeTab === 'invoices' && (
        <div className="card glass-card p-4 border-0 shadow-sm">
          <h5 className="fw-bold mb-3">Financial Invoices Log</h5>
          <div className="table-responsive">
            <table className="table align-middle">
              <thead>
                <tr>
                  <th>Invoice ID</th>
                  <th>Booking ID</th>
                  <th>Final Amount</th>
                  <th>Tax</th>
                  <th>Issue Date</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {invoices.map(inv => (
                  <tr key={inv.invoiceId}>
                    <td><strong>#I{inv.invoiceId}</strong></td>
                    <td>#B{inv.bookingId}</td>
                    <td><strong>${inv.finalAmount}</strong></td>
                    <td>${inv.taxAmount}</td>
                    <td>{inv.issueDate}</td>
                    <td>
                      <span className={`badge bg-${inv.status === 'PAID' ? 'success' : 'danger'}`}>
                        {inv.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminCrud;
