import { useNotification } from '@context/NotificationContext';
import "@css/workspace/details";
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import fetchWithToken from '@utils/fetchWithToken.ts';
import getBody from "@utils/getBody";
import React, { useEffect, useState } from 'react';
import { IoMdRefresh } from "react-icons/io";
import { MdEdit } from "react-icons/md";
import { useParams } from 'react-router-dom';
import { Input } from "reactstrap";

export default function WorkspaceUsers() {
    const { showMessage } = useNotification();

    const [users, setUsers] = useState([]);
    const [editedAlias, setEditedAlias] = useState({});
    const { name } = useParams();

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetchWithToken(`/api/v1/relation/workspace/${name}/user_alias`);
                const data = await getBody(response);
                setUsers(data);
                setEditedAlias(arrayToObject(data));
            } catch (error) {
                showMessage({
                    message: error.message
                });
            }
        };

        fetchUsers();
    }, [name]);

    const refreshAlias = async () => {
        try {
            const response = await fetchWithToken(`/api/v1/relation/workspace/${name}/user_alias/refresh`, {
                method: "PUT",
            });
            const data = await getBody(response);
            setUsers(data);
            setEditedAlias(arrayToObject(data));
        } catch (error) {
            showMessage({
                message: error.message
            });
        }
    };

    const updateUser = async (id) => {
        try {
            const aliasToUpdate = editedAlias[id] || "";

            const response = await fetchWithToken(`/api/v1/relation/workspace/${name}/user_alias`, {
                method: "PUT",
                body: JSON.stringify({ id, githubUser: aliasToUpdate }),
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            const message = await getBody(response);

            showMessage({
                type: "info",
                message
            })
            await refreshAlias();
        } catch (error) {
            showMessage({
                message: error.message
            });
        }
    };

    const arrayToObject = (arr) => {
        return arr.reduce((acc, item) => {
            acc[item.id] = item.alias ?? "";
            return acc;
        }, {});
    };

    const handleAliasChange = (id, value) => {
        setEditedAlias(prevState => ({
            ...prevState,
            [id]: value,
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
                                        value={editedAlias[id] ?? ""}
                                        placeholder={alias ?? ""}
                                        onChange={(e) => handleAliasChange(id, e.target.value)}
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
