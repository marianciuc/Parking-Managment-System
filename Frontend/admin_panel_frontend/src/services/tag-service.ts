import {TagDomain} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient from "@/http";

const TAGS_ENDPOINTS = {
    SEARCH_TAGS: () => `/api/v1/tags/search`,
    CREATE: () => `/api/v1/tags`,
    FETCH_TAG: (id: string) => `/api/v1/tags/${id}`,
    UPDATE: (tagId: string) => `/api/v1/tags/${tagId}`,
    DELETE: (tagId: string) => `/api/v1/tags/${tagId}`,
};


export default class TagService {

    public static async fetchTag(id: string): Promise<AxiosResponse<TagDomain>> {
        return apiClient.get(TAGS_ENDPOINTS.FETCH_TAG(id));
    }

    public static async searchTags(filter: {
        tagId: string;
        name: string
    }): Promise<AxiosResponse<TagDomain[]>> {
        return apiClient.get(TAGS_ENDPOINTS.SEARCH_TAGS(), {params: filter})
    }

    public static async createTag(data: { name: string, description: string }): Promise<AxiosResponse<TagDomain>> {
        return apiClient.post(TAGS_ENDPOINTS.CREATE(), data);
    }

    public static async deleteTag(tagId: string): Promise<AxiosResponse<TagDomain>> {
        return apiClient.post(TAGS_ENDPOINTS.DELETE(tagId));
    }
}