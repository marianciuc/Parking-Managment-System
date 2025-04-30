import {APIKey, ToastMessage, Page, APIKeyListElement} from "@/interfaces";
import {makeAutoObservable} from "mobx";
import {handleAxiosError} from "@/lib/exception-handler";
import APIKeysService from "@/services/api-keys-service";


export default class ApiKeysStore {
    private currentApiKey: APIKey | null = null;
    private apiKeys: APIKeyListElement[] = [];
    private currentPage: number = 0;
    private totalPages: number = 1;
    private size: number = 10;

    public constructor() {
        makeAutoObservable(this);
    }

    public get totalPagesCount(): number {
        return this.totalPages;
    }

    public get currentPageNumber(): number {
        return this.currentPage;
    }

    public get sizePerPage(): number {
        return this.size;
    }

    public get apiKeysList(): APIKeyListElement[] {
        return this.apiKeys;
    }

    public get apiKeyDetails(): APIKey | null {
        return this.currentApiKey;
    }

    private setApiKeys(page: Page<APIKeyListElement>) {
        this.apiKeys = page.content;
        this.currentPage = page.number;
        this.totalPages = page.size;
        this.size = page.total;
    }

    public async fetchKeys(filter: {
        page: number;
        size: number;
        id: string;
        status: string;
        parkingId: string;
    }): Promise<ToastMessage | void> {
        try {
            const res = await APIKeysService.searchAPIKey(filter);
            const data: Page<APIKeyListElement> = res.data;
            this.setApiKeys(data);
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }

    public async fetchApiKeyDetails(apiKeyId: string): Promise<ToastMessage | void> {
        try {
            const res = await APIKeysService.fetchAPIKey(apiKeyId);
            const data: APIKey = res.data;
            this.currentApiKey = data;
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }

    public async revokeKey(apiKeyId: string): Promise<ToastMessage | void> {
        try {
            const res = await APIKeysService.revokeAPIKey(apiKeyId);
            const data: APIKey = res.data;
            this.currentApiKey = data;
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }

    public async generateKey(parkingId: string): Promise<ToastMessage> {
        try {
            const res = await APIKeysService.generateAPIKey(parkingId);
            const data: APIKey = res.data;
            this.apiKeys.push(data);
            return {
                message: "API Key Generated Successfully",
                messageType: "success",
                statusCode: 200
            } as ToastMessage;
        } catch (err) {
            return handleAxiosError(err);
        }
    }
}

