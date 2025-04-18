import "@css/repositories/repository/userConfiguration";
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import fetchWithToken from '@utils/fetchWithToken.ts';
import React, { useEffect, useState } from 'react';
import { IoMdRefresh } from "react-icons/io";
import { MdEdit } from "react-icons/md";
import { useParams } from 'react-router-dom';
import EditAlias from './EditAlias';

export default function UserConfiguration(){
    const [users, setUsers] = useState({});
    const [editAliasModal, setEditAliasModal] = useState({})
    const {owner, repo} = useParams();

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetchWithToken(`/api/v1/relation/repository/${owner}/${repo}/user_alias`);
                const data = await response.json();
                setUsers(data);
                setEditAliasModal(Object.fromEntries(data.map(item => [item, false])));
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        };

        fetchUsers();
    }, []);

    const refreshAlias = async () => {
        try {
            const response = await fetchWithToken(`/api/v1/relation/repository/${owner}/${repo}/user_alias/refresh`, {
                method: "PUT",
            });
            const data = await response.json();
            setUsers(data);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const openModal = (username) => {
        return () => setEditAliasModal({...editAliasModal, [username]: true})
    }

    const closeModal = (username) => {
        return () => setEditAliasModal({...editAliasModal, [username]: false})
    }


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
                            <TableCell className="table-header">Username</TableCell>
                            <TableCell className="table-header">Alias</TableCell>
                            <TableCell className="table-header"></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {Object.entries(users).map(([key, value]) => (
                            <TableRow key={key}>
                                <TableCell className="user-id-cell">{key}</TableCell>
                                <TableCell className="alias-cell">
                                    <div className="alias-content">
                                        <div className="alias-list">
                                            {value.length === 0 ? "Sin alias" : value.map(el => `"${el}"`).join(", ")}
                                        </div>
                                    </div>
                                    <EditAlias
                                        keyName={key}
                                        state={users}
                                        setState={setUsers}
                                        visible={editAliasModal[key]}
                                        onClose={closeModal(key)}
                                    />
                                </TableCell>
                                <TableCell>
                                    <MdEdit
                                        onClick={openModal(key)}
                                        className="edit-icon"
                                    />
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};
