import {Page, ToastMessage, OwnerDomain} from "@/interfaces";
import {makeAutoObservable} from "mobx";
import OwnerDetailsService from "@/services/owner-details-service";
import {handleAxiosError} from "@/lib/exception-handler";


/**
 * The OwnersStore class is responsible for managing the state and operations related to parking owners.
 * It stores a list of owners, pagination details, and provides methods to fetch or search owner data.
 */
export default class OwnersStore {
    private currentOwner: OwnerDomain | null = null;
    private owners: OwnerDomain[] = [];
    private currentPage: number = 0;
    private totalPages: number = 1;
    private size: number = 10;

    public get totalPagesCount(): number {
        return this.totalPages;
    }

    public get getOwners(): OwnerDomain[] {
        return this.owners;
    }

    public get currentPageNumber(): number {
        return this.currentPage;
    }

    public get sizePerPage(): number {
        return this.size;
    }

    public get currentOwnerDetails(): OwnerDomain | null {
        return this.currentOwner;
    }

    public constructor() {
        makeAutoObservable(this);
    }

    private setOwners(page: Page<OwnerDomain>) {
        this.currentPage = page.number;
        this.totalPages = page.size;
        this.size = page.total;
        this.owners = page.content;
    }

    /**
     * Searches for owners based on the specified filter criteria.
     *
     * @param {Object} filter - The filter object containing search criteria.
     * @param {string} filter.email - The email address to search for.
     * @param {number} filter.page - The page number for paginated results.
     * @param {number} filter.size - The number of results per page.
     * @param {string} filter.id - The unique identifier to search for.
     * @param {string} filter.country - The country to search within.
     * @param {string} filter.city - The city to search within.
     * @param {string} filter.phoneNumber - The phone number to search for.
     * @return {Promise<ToastMessage | void>} A promise that resolves with a toast message or undefined.
     */
    public async searchOwners(filter: {
        email: string;
        page: number;
        size: number;
        id: string;
        country: string;
        city: string;
        phoneNumber: string
    }): Promise<ToastMessage | void> {
        try {
            const res = await OwnerDetailsService.searchOwners(filter);
            const data: Page<OwnerDomain> = res.data;
            this.setOwners(data);
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }


    /**
     * Fetches the owner details using the provided owner ID.
     * Updates the current owner information on successful retrieval.
     * Handles any errors encountered during the fetch operation.
     *
     * @param {string} ownerId - The unique identifier of the owner whose details are to be fetched.
     * @return {Promise<ToastMessage | void>} A promise that resolves to a toast message if an error occurs, or void if successful.
     */
    public async fetchOwnerDetails(ownerId: string): Promise<ToastMessage | void> {
        try {
            const res = await OwnerDetailsService.fetchOwnerDetails(ownerId);
            const data: OwnerDomain = res.data;
            this.currentOwner = data;
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }


}

