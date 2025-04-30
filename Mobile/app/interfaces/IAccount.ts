export interface Account {
    id: string;
    creationDate: string;
    modificationDate: string;
    preferredCurrency: string;
    balance: number;
    ownerId: string;
    accountType: AccountType;
    stripeAccountId: string;
    isVerifiedAccount: boolean;
}

export enum AccountType {
    STANDARD = "STANDARD",
}