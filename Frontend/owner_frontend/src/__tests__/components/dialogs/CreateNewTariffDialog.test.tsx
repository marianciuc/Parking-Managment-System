import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import { CreateNewTariffDialog } from "@/components/dialogs/CreateNewTariffDialog";
import { TariffClass } from "@/models/common/ITariffRecord";

describe("CreateNewTariffDialog Component", () => {
    const mockOnAddTariff = vi.fn();

    it("renders the dialog trigger button", () => {
        render(<CreateNewTariffDialog onAddTariff={mockOnAddTariff} />);
        const triggerButton = screen.getByText("Create");
        expect(triggerButton).toBeInTheDocument();
    });

    it("opens the dialog when the trigger button is clicked", () => {
        render(<CreateNewTariffDialog onAddTariff={mockOnAddTariff} />);
        const triggerButton = screen.getByText("Create");
        fireEvent.click(triggerButton);

        expect(
            screen.getByText("Create New Tariff")
        ).toBeInTheDocument(); // Dialog title
    });

    it("validates and submits the form correctly", async () => {
        render(<CreateNewTariffDialog onAddTariff={mockOnAddTariff} />);
        const triggerButton = screen.getByText("Create");
        fireEvent.click(triggerButton);

        // Fill in the form fields
        const nameInput = screen.getByPlaceholderText("Enter tariff name");
        fireEvent.change(nameInput, { target: { value: "Test Tariff" } });

        const descriptionInput = screen.getByPlaceholderText(
            "Enter tariff description"
        );
        fireEvent.change(descriptionInput, {
            target: { value: "This is a test tariff." },
        });

        const selectTrigger = screen.getByText("Select tariff class");
        fireEvent.click(selectTrigger);

        const selectItem = screen.getByRole("option", { name: TariffClass.FOR_WHITELISTED });
        fireEvent.click(selectItem);

        const priceInput = screen.getByPlaceholderText("Enter tariff price");
        fireEvent.change(priceInput, { target: { value: "100.00" } });

        // Submit the form
        const submitButton = screen.getByText("Create Tariff");
        fireEvent.click(submitButton);

        // Ensure the `onAddTariff` callback is called with the correct data
        await waitFor(() => {
            expect(mockOnAddTariff).toHaveBeenCalledWith({
                id: null,
                creationDate: null,
                modificationDate: null,
                name: "Test Tariff",
                tariffClass: TariffClass.FOR_WHITELISTED,
                description: "This is a test tariff.",
                price: 100,
                parkingId: null,
                recordStatus: null,
                version:null,
                currency:null,
            });
        });
    });

    it("shows validation errors when submitting an invalid form", async () => {
        render(<CreateNewTariffDialog onAddTariff={mockOnAddTariff} />);
        const triggerButton = screen.getByText("Create");
        fireEvent.click(triggerButton);

        // Try submitting the form without filling it in
        const submitButton = screen.getByText("Create Tariff");
        fireEvent.click(submitButton);

        // Ensure validation messages are displayed
        await waitFor(() => {
            expect(screen.getAllByText("This field is required").length).toBe(3);
        });

        // expect(mockOnAddTariff).not.toHaveBeenCalled(); // Ensure callback isn't triggered
    });

    it("closes the dialog after submission", async () => {
        render(<CreateNewTariffDialog onAddTariff={mockOnAddTariff} />);
        const triggerButton = screen.getByText("Create");
        fireEvent.click(triggerButton);

        // Fill in the form fields
        const nameInput = screen.getByPlaceholderText("Enter tariff name");
        fireEvent.change(nameInput, { target: { value: "Test Tariff" } });

        const descriptionInput = screen.getByPlaceholderText(
            "Enter tariff description"
        );
        fireEvent.change(descriptionInput, {
            target: { value: "This is a test tariff." },
        });

        const selectTrigger = screen.getByText("Select tariff class");
        fireEvent.click(selectTrigger);

        const selectItem = screen.getByRole("option", { name: TariffClass.FOR_WHITELISTED });
        fireEvent.click(selectItem);

        const priceInput = screen.getByPlaceholderText("Enter tariff price");
        fireEvent.change(priceInput, { target: { value: "100.00" } });

        // Submit the form
        const submitButton = screen.getByText("Create Tariff");
        fireEvent.click(submitButton);

        // Ensure the dialog is closed after submission
        await waitFor(() => {
            expect(screen.queryByText("Create New Tariff")).not.toBeInTheDocument();
        });
    });
});
