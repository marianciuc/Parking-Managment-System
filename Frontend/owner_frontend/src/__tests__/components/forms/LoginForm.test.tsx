import { describe, it, vi, expect, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { LoginForm } from "@/components/forms/LoginForm";
import { Context } from "@/main.tsx";
import UserStore from "@/store/user-store.ts";
import ParkingStore from "@/store/parking-store.ts";
import TariffStore from "@/store/tariff-store.ts";
import SessionStore from "@/store/session-store.ts";
import ReviewStore from "@/store/review-store.ts";
import AccessManagementStore from "@/store/access-management-store.ts";
import GatesManagementStore from "@/store/gates-manegement-store.ts";
import PaymentsStore from "@/store/payments-store.ts";


const mockUserStore = {
    isAuth: false,
    user: {},
    login: vi.fn(async () => Promise.resolve()),
    logout: vi.fn(),
    setAuth: vi.fn(),
    setUser: vi.fn(),
    fetchUserData: vi.fn(),
};

const mockParkingStore = new ParkingStore();
const mockTariffStore = new TariffStore();
const mockSessionStore = new SessionStore();
const mockReviewStore = new ReviewStore();
const mockAccessManagementStore = new AccessManagementStore();
const gatesManagementStore = new GatesManagementStore();
const paymentsStore = new PaymentsStore();


const renderComponent = () => {
    return render(
        <Context.Provider value={{
            userStore: mockUserStore as unknown as UserStore,
            parkingStore: mockParkingStore,
            tariffStore: mockTariffStore,
            sessionStore: mockSessionStore,
            reviewStore: mockReviewStore,
            accessManagementStore: mockAccessManagementStore,
            gatesManagementStore: gatesManagementStore,
            paymentsStore:paymentsStore,
        }}>
            <LoginForm />
        </Context.Provider>
    );
};

describe("LoginForm", () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    it("renders email and password input fields", () => {
        renderComponent();
        const emailInputs = screen.getAllByLabelText(/Email/i);
        const emailInput = emailInputs[0]; // Use the first one if it's Sign In
        const passwordInputs = screen.getAllByLabelText(/Password/i);
        const passwordInput = passwordInputs[0]; // Use the first one if it's Sign In


        expect(emailInput).toBeInTheDocument();
        expect(passwordInput).toBeInTheDocument();
    });

    it("disables submit button initially", () => {
        renderComponent();

        const submitButton = screen.getAllByRole("button", { name: /sign in/i })[0];
        expect(submitButton).toBeDisabled();
    });

    it("fills in the email input", async () => {
        renderComponent();

        // Use getByRole or data-testid to avoid multiple matches
        const emailInput = screen.getAllByRole("textbox", { name: /email/i })[0];

        await userEvent.type(emailInput, "test@example.com");

        expect(emailInput).toHaveValue("test@example.com");
    });

});
