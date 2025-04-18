import fetchWithToken from "@utils/fetchWithToken";
import { Button, Input, List, Modal, Space } from "antd";
import React, { useState } from "react";
import { useParams } from "react-router-dom";

const EditAlias = ({ state, setState, keyName, visible, onClose }) => {

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
        await fetchWithToken(`/api/v1/relation/repository/${owner}/${repo}/user_alias`, {
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
            console.error(error)
        }
    };

    return (
        <Modal
            title="Edit Alias"
            open={visible}
            onCancel={onClose}
            footer={[
                <Button key="cancel" onClick={onClose}>
                    Cancel
                </Button>,
                <Button key="accept" type="primary" onClick={handleAccept}>
                    Accept
                </Button>,
            ]}
        >
            <List
                dataSource={localValues}
                renderItem={(item, index) => (
                    <List.Item
                        actions={[
                            <Button type="link" onClick={() => handleDelete(index)}>
                                Delete
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
                    Add
                </Button>
            </Space>
        </Modal>
    );
};

export default EditAlias;
