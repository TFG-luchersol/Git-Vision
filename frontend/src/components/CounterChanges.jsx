import React from "react";
import { FaSquareMinus, FaSquarePlus } from "react-icons/fa6";
import { PiPlusMinusFill } from "react-icons/pi";


export default function CounterChanges({iconSize = 30, numberSize = 20, additions = 0, deletions = 0 }) {

    const changeStyle = { display: "flex", alignItems: "center" }
    const iconStyle = { fontSize: iconSize, marginRight: 5 }
    const numberStyle = { fontSize: numberSize, marginTop: 15 }
    return (<div style={{ display: "flex", justifyContent: "space-between"}}>
        {
            additions > 0 || deletions > 0 ? <>
                <div style={changeStyle}>{additions > 0 && <>
                    <FaSquarePlus style={iconStyle} color="green" />
                    <p style={numberStyle}>
                        {additions}
                    </p>
                </>}</div>
                <div style={changeStyle}>{deletions > 0 && <>
                    <FaSquareMinus style={iconStyle} color="red" />
                    <p style={numberStyle}>
                        {deletions}
                    </p>
                </>
                }</div>
                <div style={changeStyle}>{additions > 0 && deletions > 0 && <>
                    <PiPlusMinusFill style={iconStyle} color="orange" />
                    <p style={numberStyle}>
                        {additions + deletions}
                    </p>
                </>
                }</div></> : "SIN CAMBIOS"
}


    </div >);
}
