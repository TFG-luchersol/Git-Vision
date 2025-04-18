import "@css/workspace/details";
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import fetchWithToken from '@utils/fetchWithToken.ts';
import React, { useEffect, useState } from 'react';
import { IoMdRefresh } from "react-icons/io";
import { MdEdit } from "react-icons/md";
import { useParams } from 'react-router-dom';
import { Input } from "reactstrap";

export default function WorkspaceUsers() {
    const [users, setUsers] = useState([]);
    const [editedAlias, setEditedAlias] = useState({});  // Para manejar los alias editados
    const { name } = useParams();

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetchWithToken(`/api/v1/relation/workspace/${name}/user_alias`);
                const data = await response.json();
                setUsers(data);
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        };

        fetchUsers();
    }, [name]);

    const refreshAlias = async () => {
        try {
            const response = await fetchWithToken(`/api/v1/relation/workspace/${name}/user_alias/refresh`, {
                method: "PUT",
            });
            const data = await response.json();
            setUsers(data);
        } catch (error) {
            console.error('Error refreshing users:', error);
        }
    };

    const updateUser = async (id) => {
        try {
            const aliasToUpdate = editedAlias[id] || "";  // Si no hay alias editado, toma el valor vacío

            const response = await fetchWithToken(`/api/v1/relation/workspace/${name}/user_alias`, {
                method: "PUT",
                body: JSON.stringify({ id, githubUser: aliasToUpdate }),
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            const data = await response.json();
            setUsers(data);
            setEditedAlias(prevState => {
                const newState = { ...prevState };
                delete newState[id];  // Eliminar el alias editado para que vuelva a su estado inicial
                return newState;
            });
        } catch (error) {
            console.error('Error updating user:', error);
        }
    };

    const handleAliasChange = (id, value) => {
        setEditedAlias(prevState => ({
            ...prevState,
            [id]: value,  // Guardar el alias editado para ese usuario
        }));
    };

    return (
        <div className="user-table-wrapper">
            <div className="header">
                <h2>Lista de Usuarios</h2>
                <IoMdRefresh onClick={refreshAlias} className="refresh-icon" />
            </div>

            <TableContainer component={Paper} className="table-container">
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell className="table-header">Name</TableCell>
                            <TableCell className="table-header">Email</TableCell>
                            <TableCell className="table-header">Github User</TableCell>
                            <TableCell className="table-header"></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users.map(({ id, alias, userProfile }) => (
                            <TableRow key={id}>
                                <TableCell className="user-id-cell">{userProfile.name}</TableCell>
                                <TableCell className="user-id-cell">{userProfile.email}</TableCell>
                                <TableCell className="alias-cell">
                                    <Input
                                        className="alias-content"
                                        value={editedAlias[id] || alias}  // Mostrar el alias editado o el valor original
                                        onChange={(e) => handleAliasChange(id, e.target.value)}  // Actualizar el alias en el estado
                                    />
                                </TableCell>
                                <TableCell>
                                    <MdEdit className="edit-icon" onClick={() => updateUser(id)} />
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
