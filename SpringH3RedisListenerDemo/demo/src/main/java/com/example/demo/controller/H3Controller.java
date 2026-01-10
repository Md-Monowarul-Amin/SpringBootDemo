package com.example.demo.controller;

import com.uber.h3core.H3Core;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class H3Controller {

    private final H3Core h3;

    public H3Controller() throws IOException {
        this.h3 = H3Core.newInstance();
    }

    /**
     * Encode latitude & longitude to H3 index
     * Example:
     * /h3/encode?lat=23.8103&lon=90.4125&res=9
     */
    @GetMapping("/h3/encode")
    public String encode(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "9") int res
    ) {
        long index = h3.latLngToCell(lat, lon, res);
        return h3.h3ToString(index);
    }

    /**
     * Decode H3 index to center latitude & longitude
     * Example:
     * /h3/decode?index=8a2a1072b59ffff
     */
    @GetMapping("/h3/decode")
    public String decode(@RequestParam String index) {
        long h3Index = h3.stringToH3(index);
        var coord = h3.cellToLatLng(h3Index);
        return "lat=" + coord.lat + ", lon=" + coord.lng;
    }

    /**
     * Get neighbor cells (k-ring)
     * Example:
     * /h3/neighbors?index=8a2a1072b59ffff&k=1
     */
    @GetMapping("/h3/neighbors")
    public List<String> neighbors(
            @RequestParam String index,
            @RequestParam(defaultValue = "1") int k
    ) {
        long h3Index = h3.stringToH3(index);

        return h3.gridDisk(h3Index, k)
                .stream()
                .map(h3::h3ToString)
                .collect(Collectors.toList());
    }
}
