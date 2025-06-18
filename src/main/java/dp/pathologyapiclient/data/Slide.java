package dp.pathologyapiclient.data;

import com.google.gson.annotations.SerializedName;

public class Slide {
    
    @SerializedName("RequestId")
    public String requestId;
    @SerializedName("ExamId")
    public String examId;
    @SerializedName("Description")
    public String description;
    @SerializedName("AdditionalOrder")
    public Boolean additionalOrder;
    @SerializedName("OrderId")
    public String orderId;
    
    @SerializedName("SpecimenBox")
    public SpecimenBox specimenBox;
    public static class SpecimenBox {
        @SerializedName("Name")
        public String name;
        @SerializedName("Anatomy")
        public String anatomy;
        @SerializedName("Description")
        public String description;
        @SerializedName("Comment")
        public String comment;
    }
    
    @SerializedName("Block")
    public Block block;
    public static class Block {
        @SerializedName("Name")
        public String name;
        @SerializedName("Description")
        public String description;
        @SerializedName("Comment")
        public String comment;
        @SerializedName("Pieces")
        public String pieces;
        @SerializedName("Location")
        public String location;
    }
    
    @SerializedName("Staining")
    public Staining staining;
    public static class Staining {
        @SerializedName("Name")
        public String name;   
    }
    
}