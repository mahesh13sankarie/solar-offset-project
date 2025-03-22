import React, { useEffect, useState } from 'react';
import axios from 'axios';

const api = 'http://localhost:3000';

const UsersPage = () => {
    const [users, setUsers] = useState([]);
    const [formData, setFormData] = useState({ name: '', email: '', role: 'user' });
    const [editingUserId, setEditingUserId] = useState(null);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);

    // Fetch users
    const fetchUsers = async () => {
        try {
            const res = await axios.get(`${api}/users`);
            setUsers(res.data);
            console.log(res.data);
        } catch (err) {
            console.error('Error fetching users:', err);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // Create or update user
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingUserId) {
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
            await axios.delete(`${api}/users/${id}`);
            fetchUsers();
        } catch (err) {
            console.error('Error deleting user:', err);
        }
    };

    // Start editing
    const handleEdit = (user) => {
        setFormData({ name: user.name, email: user.email, role: user.role });
        setEditingUserId(user.id);
        setShowEditModal(true);
    };

    return (
        <div>
            <h2 className="mb-4">User Management</h2>

            <button className="btn btn-success mb-3" onClick={() => setShowCreateModal(true)}>Create User</button>

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
                                            placeholder="Name"
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
                                            <option value="user">User</option>
                                            <option value="staff">Staff</option>
                                            <option value="admin">Admin</option>
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
                                            placeholder="Name"
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
                                            <option value="user">User</option>
                                            <option value="staff">Staff</option>
                                            <option value="admin">Admin</option>
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
                {users.map((user) => (
                    <tr key={user.id}>
                        <td>{user.name}</td>
                        <td>{user.email}</td>
                        <td>
                <span className={`badge bg-${user.role === 'admin' ? 'danger' : user.role === 'staff' ? 'warning' : 'secondary'}`}>
                  {user.role}
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
                {users.length === 0 && (
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