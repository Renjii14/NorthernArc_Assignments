export interface Scan {
  id?: number;
  domainName: string;
  numPages: number;
  numBrokenLinks: number;
  numMissingImages: number;
  deleted?: boolean;
}
