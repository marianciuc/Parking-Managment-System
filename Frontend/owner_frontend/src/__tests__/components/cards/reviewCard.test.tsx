import { render, screen, fireEvent } from "@testing-library/react";
import { describe, it, expect,} from "vitest";
import ReviewCard from "@/components/cards/reviewCard";

describe("ReviewCard Component", () => {
    const mockReview = {
        id: "abc",
        name: "John Doe",
        message: "This is a sample review message.",
        date: [2025, 10 ,10 ,10 ,10 ,10, 10],
        icon_url: "https://example.com/avatar.jpg",
    };


    it("renders the fallback avatar initial when no image URL is provided", () => {

        render(
            <ReviewCard data={{ ...mockReview, icon_url: "" }} />
        );

        const avatarFallback = screen.getByText("J");
        expect(avatarFallback).toBeInTheDocument();
    });

    it("renders the 'Report' popover when the ellipsis icon is clicked", async () => {
        render(<ReviewCard data={mockReview} />);

        const ellipsisIcon = screen.getByRole("button");
        fireEvent.click(ellipsisIcon);

        // Verify that the popover content appears
        expect(screen.getByText("Report")).toBeInTheDocument();
    });


    it("applies the correct structure and classes", () => {
        render(<ReviewCard data={mockReview} />);

        const reviewCard = screen.getByRole("article", { hidden: true });
        expect(reviewCard).toHaveClass(
            "relative flex flex-row w-full justify-center px-4 items-start py-8 bg-white min-h-24 gap-3 rounded-2xl border border-gray-200"
        );

        expect(screen.getByRole("button")).toBeInTheDocument();
    });
});
