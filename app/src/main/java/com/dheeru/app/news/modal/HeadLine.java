package com.dheeru.app.news.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dkthaku on 5/27/16.
 */
public class HeadLine {


    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("print_headline")
    @Expose
    private String printHeadline;

    /**
     *
     * @return
     *     The main
     */
    public String getMain() {
        return main;
    }

    /**
     *
     * @param main
     *     The main
     */
    public void setMain(String main) {
        this.main = main;
    }

    /**
     *
     * @return
     *     The printHeadline
     */
    public String getPrintHeadline() {
        return printHeadline;
    }

    /**
     *
     * @param printHeadline
     *     The print_headline
     */
    public void setPrintHeadline(String printHeadline) {
        this.printHeadline = printHeadline;
    }
}
