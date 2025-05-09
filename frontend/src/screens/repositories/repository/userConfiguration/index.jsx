import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/userConfiguration";
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from "@utils/getBody";
import React, { useEffect, useState } from 'react';
import { IoMdRefresh } from "react-icons/io";
import { MdEdit } from "react-icons/md";
import { useParams } from 'react-router-dom';
import EditAlias from './EditAlias';

export default function UserConfiguration(){

    const { showMessage } = useNotification();
    const [users, setUsers] = useState({});
    const [editAliasModal, setEditAliasModal] = useState({})
    const {owner, repo} = useParams();

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}/user_alias`);
                const data = await getBody(response);
                setUsers(data);
                if(Object.keys(data) > 0) {
                    setEditAliasModal(Object.fromEntries(data.map(item => [item, false])));
                }
            } catch (error) {
                showMessage({
                    message: error.message
                })
            }
        };

        fetchUsers();
    }, []);

    const refreshAlias = async () => {
        try {
            const response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}/user_alias/refresh`, {
                method: "PUT",
            });
            const data = await getBody(response);
            setUsers(data);
        } catch (error) {
            showMessage({
                message: error.message
            })
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
                <IoMdRefresh onClick={refreshAlias} className="refresh-icon-repository-config" />
            </div>

            <TableContainer component={Paper} className="table-container">
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell className="table-header">Usuario Github</TableCell>
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
