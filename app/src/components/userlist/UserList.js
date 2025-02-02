import React, { useState, useEffect } from "react";
import { 
  Card, CardContent, Typography, Avatar, Button, IconButton, Container, 
  Box, Dialog, DialogTitle, DialogContent, DialogActions, TextField 
} from "@mui/material";
import { Grid } from "@mui/system";
import { Edit, Delete, Add } from "@mui/icons-material";
import { useForm, Controller } from "react-hook-form";
import * as Yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import dayjs from "dayjs";


const API_URL = "/api";

// Function to calculate age
const calculateAge = (birthDate) => {
  if (!birthDate) return "Not provided";
  const now = dayjs();
  const birth = dayjs(birthDate);
  return now.diff(birth, "year"); // Calculate the difference in years
};

// Validation with Yup
const schema = Yup.object().shape({
  name: Yup.string().required("Name is required"),
  email: Yup.string().email("Invalid email").required("Email is required"),
  birthDate: Yup.date()
    .required("Birth date is required")
    .test("age", "Age must be at least 18 years old", (value) => {
      const minAge = 18;
      const birthDate = dayjs(value);
      const today = dayjs();
      const age = today.diff(birthDate, "year");
      return age >= minAge;
    }),
});

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [open, setOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);


  const {
    control, handleSubmit, reset, formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
    defaultValues: {
      name: "",
      email: "",
      birthDate: "",
    },
  });

  // Fetching users list from API
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await fetch(API_URL + '/users');
        const data = await response.json();
        setUsers(data);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };
    fetchUsers();
  }, []);

  // Opening modal for editing
  const handleEditUser = (user) => {
    setSelectedUser(user);
    setIsEditing(true);
    reset(user); // Pre-fill form with user's data
    setOpen(true);
  };

  // Opening modal for adding new user
  const handleAddUser = () => {
    setSelectedUser({ name: "", email: "", birthDate: "", avatar: `https://i.pravatar.cc/150?u=${Date.now()}` });
    setIsEditing(false);
    reset({ name: "", email: "", birthDate: "" });
    setOpen(true);
  };

  // Closing modal
  const handleClose = () => {
    setOpen(false);
    setSelectedUser(null);
  };

  // Saving changes (update or add)
  const handleSave = async (data) => {
    if (isEditing) {
      try {
        const response = await fetch(`${API_URL}/${selectedUser.id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(data),
        });
        if (response.ok) {
          const updatedUser = await response.json();
          setUsers(users.map(user => (user.id === selectedUser.id ? updatedUser : user)));
        }
      } catch (error) {
        console.error("Error updating user:", error);
      }
    } else {
      console.log(data);
      try {
        const response = await fetch(API_URL, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(data),
        });
        if (response.ok) {
          const newUser = await response.json();
          setUsers([...users, newUser]);
        }
      } catch (error) {
        console.error("Error adding user:", error);
      }
    }
    handleClose();
  };

  // Deleting user
  const handleDeleteUser = async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
      if (response.ok) {
        setUsers(users.filter((user) => user.id !== id));
      }
    } catch (error) {
      console.error("Error deleting user:", error);
    }
  };

  return (
    <Container sx={{ mt: 4 }}>
      {/* "Add New" button */}
      <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
        <Button variant="contained" startIcon={<Add />} onClick={handleAddUser}>
          Add New
        </Button>
      </Box>

      {/* User list */}
      <Grid container spacing={2}>
        {users.map((user) => (
          <Grid item xs={12} sm={6} md={4} key={user.id}>
            <Card sx={{ maxWidth: 345, p: 2, display: "flex", flexDirection: "column", alignItems: "center" }}>
              <Avatar src={user.avatar} alt={user.name} sx={{ width: 56, height: 56, mb: 2 }} />
              <CardContent sx={{ textAlign: "center" }}>
                <Typography variant="h6">{user.name}</Typography>
                <Typography color="textSecondary">{user.email}</Typography>
                <Typography color="textSecondary">Age: {calculateAge(user.birthDate)}</Typography>
              </CardContent>

              {/* "Edit" and "Delete" buttons */}
              <Box sx={{ display: "flex", gap: 1, mb: 2 }}>
                <IconButton color="primary" onClick={() => handleEditUser(user)}>
                  <Edit />
                </IconButton>
                <IconButton color="error" onClick={() => handleDeleteUser(user.id)}>
                  <Delete />
                </IconButton>
              </Box>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Modal (used for both editing and adding) */}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{isEditing ? "Edit User" : "Add New User"}</DialogTitle>
        <DialogContent>
          {/* Name field */}
          <Controller
            name="name"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                margin="dense"
                label="Name"
                fullWidth
                error={!!errors.name}
                helperText={errors.name?.message}
              />
            )}
          />

          {/* Email field */}
          <Controller
            name="email"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                margin="dense"
                label="Email"
                fullWidth
                error={!!errors.email}
                helperText={errors.email?.message}
              />
            )}
          />

          {/* Birth Date field */}
          <Controller
            name="birthDate"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                margin="dense"
                label="Birth Date"
                type="date"
                fullWidth
                error={!!errors.birthDate}
                helperText={errors.birthDate?.message}
                InputLabelProps={{ shrink: true }}
              />
            )}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit(handleSave)} variant="contained">
            {isEditing ? "Save" : "Add"}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default UserList;