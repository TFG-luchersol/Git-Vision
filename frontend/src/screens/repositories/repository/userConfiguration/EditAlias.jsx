import { useNotification } from '@context/NotificationContext';
import fetchBackend from "@utils/fetchBackend";
import { Button, Input, List, Modal, Space } from "antd";
import React, { useState } from "react";
import { useParams } from "react-router-dom";

const EditAlias = ({ state, setState, keyName, visible, onClose }) => {
    
    const { showMessage } = useNotification();
    const {owner, repo} = useParams();

    const [localValues, setLocalValues] = useState(state[keyName] || []);
    const [newValue, setNewValue] = useState("");

    const handleAdd = () => {
        if (newValue.trim() !== "") {
            setLocalValues([...localValues, newValue.trim()]);
            setNewValue("");
        }
    };

    const handleDelete = (index) => {
        setLocalValues(localValues.filter((_, i) => i !== index));
    };

    const handleEdit = (index, value) => {
        const updatedValues = [...localValues];
        updatedValues[index] = value;
        setLocalValues(updatedValues);
    };

    const updateAlias = async () => {
        await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}/user_alias`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "username": keyName,
                "aliases": localValues
            })
        });
    }

    const handleAccept = () => {
        try {
            updateAlias();
            setState({ ...state, [keyName]: localValues });
            onClose();
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    };

    return (
        <Modal
            title="Editar Alias"
            open={visible}
            onCancel={onClose}
            footer={[
                <Button key="cancel" onClick={onClose}>
                    Cancelar
                </Button>,
                <Button key="accept" type="primary" onClick={handleAccept}>
                    Aceptar
                </Button>,
            ]}
        >
            <List
                dataSource={localValues}
                renderItem={(item, index) => (
                    <List.Item
                        actions={[
                            <Button type="link" onClick={() => handleDelete(index)}>
                                Borrar
                            </Button>,
                        ]}
                    >
                        <Input
                            value={item}
                            onChange={(e) => handleEdit(index, e.target.value)}
                        />
                    </List.Item>
                )}
            />
            <Space style={{ marginTop: 16 }}>
                <Input
                    placeholder="Add new value"
                    value={newValue}
                    onChange={(e) => setNewValue(e.target.value)}
                />
                <Button type="primary" onClick={handleAdd}>
                    Añadir
                </Button>
            </Space>
        </Modal>
    );
};

export default EditAlias;
