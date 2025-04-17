import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './UserSection.css'

const api = 'http://localhost:8000/api/v1/dashboard';

const UsersPage = () => {
    const [users, setUsers] = useState([]);
    const [formData, setFormData] = useState({ name: '', email: '', role: 'user' });
    const [editingUserId, setEditingUserId] = useState(null);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);

    // Add state for search, sort, and limit
    const [searchTerm, setSearchTerm] = useState('');
    const [sortByRole, setSortByRole] = useState('');
    const [displayLimit, setDisplayLimit] = useState(10);

    // Fetch users
    const fetchUsers = async () => {
        try {
            const res = await axios.get(`${api}/users`);
            setUsers(res.data.data);
            console.log(res.data.data);
        } catch (err) {
            console.error('Error fetching users:', err);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // Compute filtered, sorted, and limited users
    const filteredUsers = users
        .filter(user =>
            user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            user.email.toLowerCase().includes(searchTerm.toLowerCase())
        )
        .filter(user => (sortByRole ? user.accountType === sortByRole : true))
        .slice(0, displayLimit);

    // Create or update user
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingUserId) {
                console.log(formData);
                await axios.put(`${api}/users/${editingUserId}`, formData);
            } else {
                await axios.post(`${api}/users`, formData);
            }
            setFormData({ name: '', email: '', role: 'user' });
            setEditingUserId(null);
            setShowCreateModal(false);
            setShowEditModal(false);
            fetchUsers();
        } catch (err) {
            console.error('Error saving user:', err);
        }
    };

    // Delete user
    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this user?')) return;
        try {
            await axios.delete(`${api}/delete-user`, {
                data: { userId: id }
            });
            fetchUsers();
        } catch (err) {
            console.error('Error deleting user:', err);
        }
    };

    // Start editing
    const handleEdit = (user) => {
        setFormData({ name: user.fullName, email: user.email, role: user.accountType });
        setEditingUserId(user.id);
        setShowEditModal(true);
    };

    return (
        <div >
            <h2 className="mb-4 text-center d-flex" >User Management</h2>

            <nav className="navbar bg-light px-3 py-2 mb-4 rounded" style={{ boxShadow: '0 2px 8px rgba(0, 0, 0, 0.30)' }}>
                <div className="container-fluid d-flex align-items-center justify-content-between flex-wrap gap-2" >
                    <div className="d-flex align-items-center gap-5 flex-wrap">
                        <div className="d-flex align-items-center justify-content-between gap-1">
                            <label className="mb-0 small">Display</label>
                            <select
                                className="form-select form-select-sm"
                                value={displayLimit}
                                onChange={(e) => setDisplayLimit(Number(e.target.value))}
                                style={{ width: '70px' }}
                            >
                                <option value={5}>5</option>
                                <option value={10}>10</option>
                                <option value={20}>20</option>
                            </select>
                            <label className="mb-0 small">records</label>
                        </div>
                        <div className="d-flex align-items-center gap-1">
                            <label htmlFor="sortByRole">Sort By:</label>
                        <select
                            className="form-select form-select-sm"
                            value={sortByRole}
                            onChange={(e) => setSortByRole(e.target.value)}
                            style={{ width: '120px' }}
                        >
                            <option value="">All Roles</option>
                            <option value={0}>User</option>
                            <option value={2}>Staff</option>
                            <option value={1}>Admin</option>
                        </select>
                        </div>

                        <div className="d-flex align-items-center gap-1">
                            <label htmlFor="">Search:</label>
                            <input
                                type="text"
                                className="form-control form-control-sm"
                                placeholder="Search for users"
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                style={{ width: '180px' }}
                            />
                        </div>

                    </div>

                    <button
                        className="btn btn-warning text-white fw-semibold btn-sm"
                        onClick={() => setShowCreateModal(true)}
                    >
                        + Create User
                    </button>
                </div>
            </nav>



            {showCreateModal && (
                <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0, 0, 0, 0.5)' }}>
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Create User</h5>
                                <button type="button" className="btn-close" onClick={() => setShowCreateModal(false)}></button>
                            </div>
                            <form onSubmit={handleSubmit}>
                                <div className="modal-body">
                                    <div className="mb-2">
                                        <input
                                            type="text"
                                            className="form-control"
                                            placeholder="Full Name"
                                            value={formData.name}
                                            required
                                            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <input
                                            type="email"
                                            className="form-control"
                                            placeholder="Email"
                                            value={formData.email}
                                            required
                                            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <select
                                            className="form-select"
                                            value={formData.role}
                                            onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                                        >
                                            <option value={0}>User</option>
                                            <option value={2}>Staff</option>
                                            <option value={1}>Admin</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="submit" className="btn btn-primary">Create</button>
                                    <button type="button" className="btn btn-secondary" onClick={() => setShowCreateModal(false)}>Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}

            {showEditModal && (
                <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0, 0, 0, 0.5)' }}>
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Edit User</h5>
                                <button type="button" className="btn-close" onClick={() => setShowEditModal(false)}></button>
                            </div>
                            <form onSubmit={handleSubmit}>
                                <div className="modal-body">
                                    <div className="mb-2">
                                        <input
                                            type="text"
                                            className="form-control"
                                            placeholder="Full Name"
                                            value={formData.name}
                                            required
                                            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <input
                                            type="email"
                                            className="form-control"
                                            placeholder="Email"
                                            value={formData.email}
                                            required
                                            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <select
                                            className="form-select"
                                            value={formData.role}
                                            onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                                        >
                                            <option value={0}>User</option>
                                            <option value={2}>Staff</option>
                                            <option value={1}>Admin</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="submit" className="btn btn-primary">Update</button>
                                    <button type="button" className="btn btn-secondary" onClick={() => setShowEditModal(false)}>Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}

            <table className="table table-striped table-bordered">
                <thead className="table-dark">
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th style={{ width: '180px' }}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {filteredUsers.map((user) => (
                    <tr key={user.id}>
                        <td>{user.fullName}</td>
                        <td>{user.email}</td>
                        <td>
                <span className={`badge bg-${user.accountType === 1 ? 'danger' : user.accountType === 2 ? 'warning' : 'secondary'}`}>
                  {user.accountType === 1 ? 'Admin' : user.accountType === 2 ? 'Staff' : 'User'}
                </span>
                        </td>
                        <td>
                            <button onClick={() => handleEdit(user)} className="btn btn-sm btn-outline-primary me-2">
                                Edit
                            </button>
                            <button onClick={() => handleDelete(user.id)} className="btn btn-sm btn-outline-danger">
                                Delete
                            </button>
                        </td>
                    </tr>
                ))}
                {filteredUsers.length === 0 && (
                    <tr>
                        <td colSpan="4" className="text-center">No users found.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default UsersPage;