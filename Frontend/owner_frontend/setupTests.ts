import '@testing-library/jest-dom';
import { vi } from "vitest";

// Ensure the `root` container exists in JSDOM
const root = document.createElement("div");
root.setAttribute("id", "root");
document.body.appendChild(root);

// Mock `document.getElementById`
vi.spyOn(document, "getElementById").mockImplementation((id) => {
    return id === "root" ? root : null;
});