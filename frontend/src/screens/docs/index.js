// import React, { useEffect, useState } from 'react';
// import ReactMarkdown from 'react-markdown';
// import remarkGfm from 'remark-gfm';
// import md from '../docs/documentation.md';

// export default function CustomerAgreement() {
//     const [markdown, setMarkdown] = useState("");

//     useEffect(() => {
//         fetch(md)
//             .then(response => response.text())
//             .then(text => setMarkdown(text));
//     }, []);

//     return (
//         <div className="home-page-container">
//             <div className="markdown-container">
//                 <ReactMarkdown remarkPlugins={[remarkGfm]} children={markdown} />
//             </div>
//         </div>
//     );
// }