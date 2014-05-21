reset
OUTPUT_FILE_NAME="D:/MyProject/SpaceElectricWave/DEV/plot/latest/bin/output/PTPlot_2012030508.png"
VAR0="D:/MyProject/SpaceElectricWave/DEV/plot/latest/bin/data/PTPlot.DAT"
VAR1="Updated 2012-03-05 17:19:06 KST"
XRANGE_START="2012-03-05 00:00:00"
XRANGE_END="2012-03-05 23:59:59"
YRANGE_START=0
YRANGE_END=200
YTICS_TERM=50
TITLE_STRING="GOES Magnetometer (1 minute)"
XLABEL_STRING="Universal Time"
YLABEL_STRING="nanoTesla (nT)"
RC_NAME="  테스트 라벨"
BACKGROUND_COLOR="#ffffff"
FORE_COLOR="#000000"
MAJOR_GRID_COLOR="#000000"
MINOR_GRID_COLOR="#e0e0e0"
P1_COLOR="#ff0000"
P2_COLOR="#0000ff"
FONT_TITLE="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/alxb__ub.ttf,16"
FONT_N_12="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/alr___ub.ttf,12"
FONT_N_10="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/alr___ub.ttf,10"
FONT_N_08="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/alr___ub.ttf,8"
FONT_B_12="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/alxb__ub.ttf,12"
FONT_B_10="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/alxb__ub.ttf,10"
FONT_B_08="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/alxb__ub.ttf,8"
FONT_RC="D:/MyProject/SpaceElectricWave/DEV/plot/jGnuPlot/bin/fonts/HMBC.TTF,12"
set terminal png size 640, 480 background "#ffffff"
set output OUTPUT_FILE_NAME
set tmargin 0
set rmargin 0
set lmargin 0
set bmargin 0
set origin 0.15, 0.15
set size 0.75, 0.75
set xdata time
set timefmt "%Y-%m-%d %H:%M:%S"
set title TITLE_STRING font FONT_TITLE tc rgb FORE_COLOR enhanced
set xrange [ XRANGE_START : XRANGE_END ]
set xlabel XLABEL_STRING font FONT_B_12 tc rgb FORE_COLOR
set xtics 86400 format "%b %d" font FONT_N_12
set termoption enhanced
set ylabel YLABEL_STRING font FONT_B_12 tc rgb FORE_COLOR
set yrange [ YRANGE_START : YRANGE_END ]
set ytics YRANGE_START, YTICS_TERM, YRANGE_END font FONT_N_12 tc rgb FORE_COLOR
set mytics 10
set y2range [ YRANGE_START : YRANGE_END ]
set label RC_NAME at screen 0.01, 0.015 font FONT_RC textcolor rgb FORE_COLOR
set label VAR1 at screen 0.70, 0.015 font FONT_B_08 textcolor rgb FORE_COLOR
set border lc rgb FORE_COLOR
set style line 98 lw 1 lt 0 lc rgb MINOR_GRID_COLOR
set style line 99 lw 1 lt 1 lc rgb MAJOR_GRID_COLOR
set key off
set label "Solar Wind Speed"  at graph 1.08, 0.25 rotate by 90 font FONT_B_12 textcolor rgb P1_COLOR
set label "Solar Wind Density" at graph 1.04, 0.25 rotate by 90 font FONT_B_12 textcolor rgb P2_COLOR
set border lc rgb FORE_COLOR
set style line 99 lw 1 lt 0 lc rgb MAJOR_GRID_COLOR
set grid back xtics ytics ls 99
div=0.25
set obj rect from graph 0, graph 0 to graph 1, graph 1 fs solid noborder 0.08 fc rgb "#000000" behind
set obj rect from graph 0, graph (div)*3 to graph 1, graph (div)*4 fs solid noborder 0.08 fc rgb "#000000" behind
set obj rect from graph 0, graph (div)*2 to graph 1, graph (div)*3 fs solid noborder 0.04 fc rgb "#000000" behind
set obj rect from graph 0, graph (div)*1 to graph 1, graph (div)*2 fs solid noborder 0.08 fc rgb "#000000" behind
set obj rect from graph 0, graph (div)*0 to graph 1, graph (div)*1 fs solid noborder 0.04 fc rgb "#000000" behind
plot VAR0 using 1:3 title "0.8"  with lines lw 1.7 lt rgb P1_COLOR, VAR0 using 1:4 title "2.0"  with lines lw 1.7 lt rgb P2_COLOR
set output
