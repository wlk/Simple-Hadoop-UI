package pl.wroc.pwr.zpi.model;

public interface INetAlg {

	public abstract String getSchema();

	public abstract void setSchema(String schema);

	public abstract String getDelimiter();

	public abstract void setDelimiter(String delimiter);
	
	public abstract void save();

}