
import '@testing-library/jest-dom';
import { render, screen, fireEvent } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import Card from "@/components/cards/card.tsx"
import IParking, { ParkingStatus } from "@/models/common/IParking";

describe("Card Component", () => {
    const mockParking : IParking = {
        rating: 0, tags: [],
        id: "1",
        name: "Test Parking",
        imageUrl:'./parking.png',
        address: {
            id:"id",
            countryCode: "PL",
            city: "Test City",
            postalCode: "71450",
            street: "Test Street",
            buildingNumber: "123",
            latitude : 1,
            longitude : 1,
            isBelongToAnyInstitution: false,
            institutionName: "Test Institution",
            recordStatus: "PENDING",
            creationDate: "2021-01-01",
            modificationDate: "2021-02-01",
        },
        parkingStatus: ParkingStatus.OPEN,
        is24h: true,
        capacity: {
            capacity:20,
            occupiedSpaces: 5,
            placesForDisabled:3,
            occupiedPlacesForDisabled:1,
            placesForElectricCars:1,
            occupiedPlacesForElectricCar:0,
        },
        isPinned: false,
        recordStatus:"open",
        ownerId:"29",
        description: "Test Description"
    };

    const mockTogglePin = vi.fn();
    const mockOnClick = vi.fn();

    it("renders the card with correct parking information", () => {
        render(
            <Card
                parking={mockParking}
                togglePin={mockTogglePin}
                onClick={mockOnClick}
            />
        );

        // Verify parking name and address
        expect(screen.getByText(mockParking.name)).toBeInTheDocument();
        expect(
            screen.getByText(
                `${mockParking.address.countryCode}, ${mockParking.address.city}, ${mockParking.address.street}, ${mockParking.address.buildingNumber}`
            )
        ).toBeInTheDocument();

        // Verify capacity info
        expect(
            screen.getByText(
                `${mockParking.capacity.occupiedSpaces}/${mockParking.capacity.capacity}`
            )
        ).toBeInTheDocument();

        // Verify status
        expect(screen.getByText("OPEN")).toBeInTheDocument();
    });

    it("applies the correct background class for OPEN status", () => {
        render(
            <Card
                parking={{ ...mockParking, parkingStatus: ParkingStatus.OPEN }}
                togglePin={mockTogglePin}
            />
        );

        const buttonElement = screen.getByRole("button");
        expect(buttonElement).toHaveClass("open_parking_background");
    });

    it("applies the correct background class for CLOSED status", () => {
        render(
            <Card
                parking={{ ...mockParking, parkingStatus: ParkingStatus.CLOSED }}
                togglePin={mockTogglePin}
            />
        );

        const buttonElement = screen.getByRole("button");
        expect(buttonElement).toHaveClass("close_parking_background");
    });

    it("applies the correct background class for CREATION_PROCESS status", () => {
        render(
            <Card
                parking={{ ...mockParking, parkingStatus: ParkingStatus.CREATION_PROCESS }}
                togglePin={mockTogglePin}
            />
        );

        const buttonElement = screen.getByRole("button");
        expect(buttonElement).toHaveClass("creation_process_background");
    });

    it("triggers onClick when the card is clicked", () => {
        render(
            <Card
                parking={mockParking}
                togglePin={mockTogglePin}
                onClick={mockOnClick}
            />
        );

        const buttonElement = screen.getByRole("button");
        fireEvent.click(buttonElement);

        expect(mockOnClick).toHaveBeenCalled();
    });

});
