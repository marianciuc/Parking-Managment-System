
export interface APIKeyListElement {
    id: string;
    scope: string[];
    status: string;
    issuedBy: string;
    revokedBy: string;
    parkingId: string;
}


export interface APIKey {
    id: string;
    scope: string[];
    status: string;
    key: string;
    parkingId: string;
    issuedBy: string;
    revokedBy: string;
    creationDate: Date;
    modificationDate: Date;
}