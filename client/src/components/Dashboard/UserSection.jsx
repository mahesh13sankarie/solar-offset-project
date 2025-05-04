import React, { useEffect, useState } from "react";
import { api } from "../../api";
import "./UserSection.css";

const UsersPage = () => {
    const [users, setUsers] = useState([]);
    const [formData, setFormData] = useState({ name: "", email: "", role: "1", password: "" });
    const [editingUserId, setEditingUserId] = useState(null);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);

    // Add state for search, sort, and limit
    const [searchTerm, setSearchTerm] = useState("");
    const [sortByRole, setSortByRole] = useState("");
    const [displayLimit, setDisplayLimit] = useState(10);

    // Fetch users
    const fetchUsers = async () => {
        try {
            const res = await api.dashboard.getUsers();
            const userData = Array.isArray(res.data.data) ? res.data.data : [];
            setUsers(userData);
            console.log(userData);
        } catch (err) {
            console.error("Error fetching users:", err);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // Compute filtered, sorted, and limited users
    const filteredUsers = (users || [])
        .filter(
            (user) =>
                user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                user.email.toLowerCase().includes(searchTerm.toLowerCase()),
        )
        .filter((user) => (sortByRole !== "" ? user.accountType === sortByRole : true))
        .slice(0, displayLimit);

    // Create or update user
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingUserId) {
                // Update user role
                await api.dashboard.updateRole({
                    userId: editingUserId,
                    accountType: Number(formData.role),
                });
                setEditingUserId(null);
                setShowEditModal(false);
            } else {
                // Create user via external API
                const payload = {
                    email: formData.email,
                    password: formData.password, // Add password to the payload
                    fullName: formData.name,
                    accountType: Number(formData.role),
                };
                await api.auth.register(payload);
                setShowCreateModal(false);
            }
            setFormData({ name: "", email: "", role: "1", password: "" }); // Reset form data including password
            fetchUsers();
        } catch (err) {
            console.error("Error creating user:", err);
            // You can add a user-facing error display here if desired
        }
    };

    // Delete user
    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this user?")) return;
        try {
            await api.dashboard.deleteUser(id);
            fetchUsers();
        } catch (err) {
            console.error("Error deleting user:", err);
        }
    };

    // Start editing
    const handleEdit = (user) => {
        setFormData({ name: user.fullName, email: user.email, role: user.accountType.toString() });
        setEditingUserId(user.id);
        setShowEditModal(true);
    };

    return (
        <div>
            <h2 className="mb-4 text-center d-flex">User Management</h2>

            <nav
                className="navbar bg-light px-3 py-2 mb-4 rounded"
                style={{ boxShadow: "0 2px 8px rgba(0, 0, 0, 0.30)" }}
            >
                <div className="container-fluid d-flex align-items-center justify-content-between flex-wrap gap-2">
                    <div className="d-flex align-items-center gap-5 flex-wrap">
                        <div className="d-flex align-items-center justify-content-between gap-1">
                            <label className="mb-0 small">Display</label>
                            <select
                                className="form-select form-select-sm"
                                value={displayLimit}
                                onChange={(e) => setDisplayLimit(Number(e.target.value))}
                                style={{ width: "70px" }}
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
                                onChange={(e) =>
                                    setSortByRole(
                                        e.target.value === "" ? "" : Number(e.target.value),
                                    )
                                }
                                style={{ width: "120px" }}
                            >
                                <option value="">All Roles</option>
                                <option value={1}>User</option>
                                <option value={2}>Staff</option>
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
                                style={{ width: "180px" }}
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
                <div
                    className="modal fade show d-block"
                    tabIndex="-1"
                    style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
                >
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Create User</h5>
                                <button
                                    type="button"
                                    className="btn-close"
                                    onClick={() => setShowCreateModal(false)}
                                ></button>
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
                                            onChange={(e) =>
                                                setFormData({ ...formData, name: e.target.value })
                                            }
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <input
                                            type="email"
                                            className="form-control"
                                            placeholder="Email"
                                            value={formData.email}
                                            required
                                            onChange={(e) =>
                                                setFormData({ ...formData, email: e.target.value })
                                            }
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <input
                                            type="password"
                                            className="form-control"
                                            placeholder="Password"
                                            value={formData.password}
                                            required
                                            onChange={(e) =>
                                                setFormData({
                                                    ...formData,
                                                    password: e.target.value,
                                                })
                                            }
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <select
                                            className="form-select"
                                            value={formData.role}
                                            onChange={(e) =>
                                                setFormData({ ...formData, role: e.target.value })
                                            }
                                        >
                                            <option value={1}>User</option>
                                            <option value={2}>Staff</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="submit" className="btn btn-primary">
                                        Create
                                    </button>
                                    <button
                                        type="button"
                                        className="btn btn-secondary"
                                        onClick={() => setShowCreateModal(false)}
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}

            {showEditModal && (
                <div
                    className="modal fade show d-block"
                    tabIndex="-1"
                    style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
                >
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Edit User</h5>
                                <button
                                    type="button"
                                    className="btn-close"
                                    onClick={() => setShowEditModal(false)}
                                ></button>
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
                                            onChange={(e) =>
                                                setFormData({ ...formData, name: e.target.value })
                                            }
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <input
                                            type="email"
                                            className="form-control"
                                            placeholder="Email"
                                            value={formData.email}
                                            required
                                            onChange={(e) =>
                                                setFormData({ ...formData, email: e.target.value })
                                            }
                                        />
                                    </div>
                                    <div className="mb-2">
                                        <select
                                            className="form-select"
                                            value={formData.role}
                                            onChange={(e) =>
                                                setFormData({ ...formData, role: e.target.value })
                                            }
                                        >
                                            <option value={1}>User</option>
                                            <option value={2}>Staff</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="submit" className="btn btn-primary">
                                        Update
                                    </button>
                                    <button
                                        type="button"
                                        className="btn btn-secondary"
                                        onClick={() => setShowEditModal(false)}
                                    >
                                        Cancel
                                    </button>
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
                        <th style={{ width: "180px" }}>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredUsers.map((user) => (
                        <tr key={user.id}>
                            <td>{user.fullName}</td>
                            <td>{user.email}</td>
                            <td>
                                <span
                                    className={`badge bg-${
                                        user.accountType === 2 ? "warning" : "secondary"
                                    }`}
                                >
                                    {user.accountType === 2 ? "Staff" : "User"}
                                </span>
                            </td>
                            <td>
                                <button
                                    onClick={() => handleEdit(user)}
                                    className="btn btn-sm btn-outline-primary me-2"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={() => handleDelete(user.id)}
                                    className="btn btn-sm btn-outline-danger"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    {filteredUsers.length === 0 && (
                        <tr>
                            <td colSpan="4" className="text-center">
                                No users found.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default UsersPage;
