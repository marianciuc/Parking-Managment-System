import {createContext, StrictMode} from 'react'
import {createRoot, Root} from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import UserStore from "@/store/user-store.ts";
import ParkingStore from "@/store/parking-store.ts";
import {Toaster} from "@/components/ui/toaster.tsx";
import TariffStore from "@/store/tariff-store.ts";
import SessionStore from "@/store/session-store.ts";
import ReviewStore from "@/store/review-store.ts";
import AccessManagementStore from "@/store/access-management-store.ts";
import GatesManagementStore from "@/store/gates-manegement-store.ts";
import PaymentsStore from "@/store/payments-store.ts";

const userStore = new UserStore();
const parkingStore = new ParkingStore();
const tariffStore = new TariffStore();
const sessionStore = new SessionStore();
const reviewStore = new ReviewStore();
const accessManagementStore = new AccessManagementStore();
const gatesManagementStore = new GatesManagementStore();
const paymentsStore = new PaymentsStore();

interface AppState {
    userStore: UserStore;
    parkingStore: ParkingStore;
    tariffStore: TariffStore;
    sessionStore: SessionStore;
    reviewStore: ReviewStore;
    accessManagementStore: AccessManagementStore;
    gatesManagementStore: GatesManagementStore;
    paymentsStore: PaymentsStore;
}

export const Context = createContext<AppState>({
    userStore,
    parkingStore,
    tariffStore,
    sessionStore,
    reviewStore,
    accessManagementStore,
    gatesManagementStore,
    paymentsStore
});

const container = document.getElementById("root");
if (!container) {
    throw new Error("Root container not found!");
}
// Check if root already exists
function getOrCreateRoot(container: HTMLElement): Root {
    let root = (container as any).__reactRoot || null;
    if (!root) {
        root = createRoot(container);
        (container as any).__reactRoot = root;
    }
    return root;
}

getOrCreateRoot(container).render(
    <StrictMode>
        <Context.Provider value={{ userStore, parkingStore, tariffStore, sessionStore,reviewStore,accessManagementStore, gatesManagementStore, paymentsStore }}>
            <App />
            <Toaster />
        </Context.Provider>
    </StrictMode>
);
